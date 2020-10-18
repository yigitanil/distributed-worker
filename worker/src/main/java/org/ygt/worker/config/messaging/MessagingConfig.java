package org.ygt.worker.config.messaging;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBinding(UrlStatusTopicChannel.class)
public class MessagingConfig {


}
