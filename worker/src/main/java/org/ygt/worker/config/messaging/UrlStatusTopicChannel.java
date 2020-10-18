package org.ygt.worker.config.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface UrlStatusTopicChannel {

    String urlStatusInputChannel = "urlStatusInputChannel";

    @Input(urlStatusInputChannel)
    SubscribableChannel urlStatusInputChannel();

}
