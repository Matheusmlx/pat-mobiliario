package br.com.azi.patrimoniomobiliario.configuration.factory.job;

import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.time.Clock;
import java.util.TimeZone;

@Configuration
@RequiredArgsConstructor
public class SchedulerLockFactory {

    private final Clock clock;

    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(
            JdbcTemplateLockProvider.Configuration.builder()
                .withJdbcTemplate(new JdbcTemplate(dataSource))
                .withTableName("pat_mobiliario.shedlock")
                .withTimeZone(TimeZone.getTimeZone(clock.getZone()))
                .build()
        );
    }
}
