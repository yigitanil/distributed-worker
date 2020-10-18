package org.ygt.worker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.ygt.data.model.enums.Status;
import org.ygt.data.model.table.UrlStatus;
import org.ygt.data.repository.UrlStatusRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrlStatusCheckService {

    private final RestTemplate restTemplate;
    private final UrlStatusRepository urlStatusRepository;

    public void checkUrlStatus(UrlStatus urlStatus) {
        log.info("{}", urlStatus);
        Optional<UrlStatus> statusOptional = urlStatusRepository.findById(urlStatus.getId());
        if (statusOptional.isPresent()) {
            UrlStatus status = statusOptional.get();
            try {
                ResponseEntity<Object> exchange = restTemplate.exchange(urlStatus.getUrl(), HttpMethod.HEAD, HttpEntity.EMPTY, Object.class);
                status.setStatus(Status.DONE);
                status.setHttpCode(exchange.getStatusCodeValue() + "");
                urlStatusRepository.save(status);

            } catch (Exception e) {
                status.setStatus(Status.ERROR);
                urlStatusRepository.save(status);
            }
            log.info("{}", status);
        }
    }
}
