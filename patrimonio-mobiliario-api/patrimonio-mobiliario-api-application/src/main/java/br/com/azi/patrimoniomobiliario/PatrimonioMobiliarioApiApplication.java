package br.com.azi.patrimoniomobiliario;

import br.com.azi.patrimoniomobiliario.configuration.PatrimonioMobiliarioProperties;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableEurekaClient
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "10m")
@EnableBatchProcessing
@EnableConfigurationProperties(PatrimonioMobiliarioProperties.class)
@SpringBootApplication
public class PatrimonioMobiliarioApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatrimonioMobiliarioApiApplication.class, args);
    }

}
