package org.ygt.worker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.ygt.data.model.enums.Status;
import org.ygt.data.model.table.UrlStatus;
import org.ygt.data.repository.UrlStatusRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class UrlStatusCheckServiceTest {

    @Mock
    RestTemplate restTemplate;
    @Mock
    UrlStatusRepository urlStatusRepository;

    UrlStatusCheckService urlStatusCheckService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        urlStatusCheckService = new UrlStatusCheckService(restTemplate, urlStatusRepository);
    }

    @Test
    void checkUrlStatusShouldSaveUrlStatusAs200AndDoneWhenRequestIsSuccessful() {
        //given
        UrlStatus urlStatus = new UrlStatus();
        urlStatus.setId(1);
        urlStatus.setUrl("http://url");
        urlStatus.setStatus(Status.NEW);

        //when
        when(urlStatusRepository.findById(urlStatus.getId()))
                .thenReturn(Optional.of(urlStatus));
        when(restTemplate.exchange(urlStatus.getUrl(), HttpMethod.HEAD, HttpEntity.EMPTY, Object.class))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        //then
        urlStatusCheckService.checkUrlStatus(urlStatus);

        verify(urlStatusRepository, times(1)).save(argThat(urlStatus1 -> {
            assertEquals("200", urlStatus1.getHttpCode());
            assertEquals(Status.DONE, urlStatus1.getStatus());
            assertEquals(urlStatus.getId(), urlStatus1.getId());
            return true;
        }));

    }

    @Test
    void checkUrlStatusShouldSaveUrlStatusAsNullAndErrorWhenRequestIsSuccessful() {
        //given
        UrlStatus urlStatus = new UrlStatus();
        urlStatus.setId(1);
        urlStatus.setUrl("http://url");
        urlStatus.setStatus(Status.NEW);

        //when
        when(urlStatusRepository.findById(urlStatus.getId()))
                .thenReturn(Optional.of(urlStatus));
        doThrow(new RuntimeException())
                .when(restTemplate).exchange(urlStatus.getUrl(), HttpMethod.HEAD, HttpEntity.EMPTY, Object.class);

        //then
        urlStatusCheckService.checkUrlStatus(urlStatus);

        verify(urlStatusRepository, times(1)).save(argThat(urlStatus1 -> {
            assertNull(urlStatus1.getHttpCode());
            assertEquals(Status.ERROR, urlStatus1.getStatus());
            assertEquals(urlStatus.getId(), urlStatus1.getId());
            return true;
        }));

    }

    @Test
    void checkUrlStatusShouldDoNothingWhenUrlStatusIsNotFound() {
        //given
        UrlStatus urlStatus = new UrlStatus();
        urlStatus.setId(1);
        urlStatus.setUrl("http://url");
        urlStatus.setStatus(Status.NEW);

        //when
        when(urlStatusRepository.findById(urlStatus.getId()))
                .thenReturn(Optional.empty());

        //then
        urlStatusCheckService.checkUrlStatus(urlStatus);

        verify(urlStatusRepository, never()).save(any());
        verify(restTemplate, never()).exchange(urlStatus.getUrl(), HttpMethod.HEAD, HttpEntity.EMPTY, Object.class);

    }
}