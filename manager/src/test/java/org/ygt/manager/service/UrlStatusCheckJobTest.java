package org.ygt.manager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.MessageChannel;
import org.ygt.data.model.enums.Status;
import org.ygt.data.model.table.UrlStatus;
import org.ygt.data.repository.UrlStatusRepository;

import java.util.List;

import static org.mockito.Mockito.*;

class UrlStatusCheckJobTest {

    @Mock
    UrlStatusRepository urlStatusRepository;
    @Mock
    MessageChannel urlStatusOutputChannel;

    UrlStatusCheckJob urlStatusCheckJob;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        urlStatusCheckJob = new UrlStatusCheckJob(urlStatusRepository, urlStatusOutputChannel);
    }

    @Test
    void runShouldUpdateTheStatusAndPublishMessage() throws Exception {
        //given
        UrlStatus urlStatus = new UrlStatus();
        urlStatus.setId(1);
        urlStatus.setUrl("http://url");
        urlStatus.setStatus(Status.NEW);

        //when
        when(urlStatusRepository.findByStatus(Status.NEW, PageRequest.of(0, 10)))
                .thenReturn(List.of(urlStatus));
        when(urlStatusRepository.save(urlStatus))
                .thenReturn(urlStatus);

        //then
        urlStatusCheckJob.run();

        verify(urlStatusOutputChannel, times(1)).send(any());

    }
}