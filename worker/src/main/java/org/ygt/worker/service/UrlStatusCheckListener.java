package org.ygt.worker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;
import org.ygt.data.model.table.UrlStatus;
import org.ygt.data.util.JsonUtil;

import static org.ygt.worker.config.messaging.UrlStatusTopicChannel.urlStatusInputChannel;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrlStatusCheckListener {

    private final UrlStatusCheckService urlStatusCheckService;

    @StreamListener(urlStatusInputChannel)
    public void handleEvent(String message) {
        log.info("Message received {}", message);
        UrlStatus urlStatus = JsonUtil.deserialize(message, UrlStatus.class);
        if (urlStatus != null) {
            urlStatusCheckService.checkUrlStatus(urlStatus);
        }

    }
}
