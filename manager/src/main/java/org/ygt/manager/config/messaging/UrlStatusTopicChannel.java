package org.ygt.manager.config.messaging;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface UrlStatusTopicChannel {

    String urlStatusOutputChannel = "urlStatusOutputChannel";

    @Output(urlStatusOutputChannel)
    MessageChannel urlStatusOutputChannel();

}
