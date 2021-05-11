package br.com.azi.patrimoniomobiliario.configuration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
@ComponentScan(basePackages = {"br.com.azi.patrimoniomobiliario.entrypoint"})
public class SwaggerConfig {

    @Value("${application.name}")
    private String projectName;

    @Value("${application.version}")
    private String projectVersion;

    @Autowired
    private ServletContext servletContext;

    @Bean
    public Docket docketSwagger() {
        return new Docket(DocumentationType.SWAGGER_2)
            .host(getDomain())
            .pathProvider(getPathProvider())
            .select()
            .apis(RequestHandlerSelectors.basePackage("br.com.azi.patrimoniomobiliario.entrypoint"))
            .build()
            .apiInfo(metaData())
            .securitySchemes(Collections.singletonList(new ApiKey("APIKey", "token-acesso", "header")))
            .securityContexts(Collections.singletonList(SecurityContext.builder()
                .securityReferences(defaultAuth()).forPaths(PathSelectors.regex("/.*"))
                .build()));
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title(projectName)
                .description("Documentação da api")
                .version(projectVersion)
                .build();
    }

    private String getDomain() {
        String domain = System.getenv("DOMAIN");
        return StringUtils.isEmpty(domain) ? "localhost" : domain;
    }

    private RelativePathProvider getPathProvider() {
        return new RelativePathProvider(servletContext) {
            @Override
            public String getApplicationBasePath() {
                return "/patrimonio-mobiliario/api/";
            }
        };
    }

    private List<SecurityReference> defaultAuth() {
        final AuthorizationScope authorizationScope = new AuthorizationScope("public", "Acesso Público");
        final AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{authorizationScope};
        return Collections.singletonList(new SecurityReference("APIKey", authorizationScopes));
    }

}
