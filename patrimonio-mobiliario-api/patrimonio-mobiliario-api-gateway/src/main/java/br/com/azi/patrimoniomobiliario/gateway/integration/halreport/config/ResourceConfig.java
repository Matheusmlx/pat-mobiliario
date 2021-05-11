package br.com.azi.patrimoniomobiliario.gateway.integration.halreport.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
public class ResourceConfig {

    @Bean
    public ClasspathResourceLoader getClassPathResource() {
        return new ClasspathResourceLoader();
    }

    public static class ClasspathResourceLoader {
        public Resource getResource(String path) {
            return new ClassPathResource(path);
        }
    }
}
