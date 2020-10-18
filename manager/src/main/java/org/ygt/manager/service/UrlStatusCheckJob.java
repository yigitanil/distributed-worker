package org.ygt.manager.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.ygt.data.model.enums.Status;
import org.ygt.data.model.table.UrlStatus;
import org.ygt.data.repository.UrlStatusRepository;
import org.ygt.data.util.JsonUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrlStatusCheckJob {

    private final UrlStatusRepository urlStatusRepository;
    private final MessageChannel urlStatusOutputChannel;

    @Scheduled(fixedRate = 2000)
    public void run() throws Exception {
        try {
            List<UrlStatus> urlStatuses = urlStatusRepository.findByStatus(Status.NEW, PageRequest.of(0, 10));
            for (UrlStatus urlStatus : urlStatuses) {
                urlStatus.setStatus(Status.PROCESSING);
                UrlStatus save = urlStatusRepository.save(urlStatus);
                Message<String> message = new GenericMessage<>(JsonUtil.serialize(save));
                urlStatusOutputChannel.send(message);
            }
        } catch (Exception e) {
            log.error("Error while getting and publishing urls", e);
        }

    }
}
