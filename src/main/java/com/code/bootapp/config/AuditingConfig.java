package com.code.bootapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.Optional;


/*
 * createTime, updateTime 공통화 설정
 */

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "auditiorDateTimeProvider")
public class AuditingConfig {

    @Bean(name = "auditiorDateTimeProvider")
    public DateTimeProvider auditiorDateTimeProvider() {
        return () -> Optional.of(LocalDateTime.now());
    }
}
