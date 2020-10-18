package org.ygt.manager.config.data;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "org.ygt.data.model.table")
@EnableJpaRepositories(basePackages = "org.ygt.data.repository")
public class DatabaseConfig {
}
