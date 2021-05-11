package br.com.azi.patrimoniomobiliario.configuration.factory.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.SessaoUsuarioDataProviderImpl;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.UsuarioReadOnlyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
public class SessaoUsuarioDataProviderFactory {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UsuarioReadOnlyRepository usuarioReadOnlyRepository;

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public SessaoUsuarioDataProvider createSessaoUsuarioDataProviderImpl() {
        return new SessaoUsuarioDataProviderImpl(
            usuarioReadOnlyRepository,
            request.getHeader("az-user"),
            request.getHeader("az-domain-id"),
            request.getHeader("az-domain-type"),
            request.getHeader("host"),
            formatarRoles(),
            formatarCookies()
        );
    }

    private List<String> formatarRoles() {
        if (Objects.nonNull(request.getHeader("az-roles"))) {
            String[] roles = request.getHeader("az-roles").split(",");
            return Arrays.stream(roles)
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private List<SessaoUsuario.Cookie> formatarCookies() {
        if (Objects.nonNull(request.getCookies())) {
            return Arrays.stream(request.getCookies())
                .map(this::formatarCookie)
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private SessaoUsuario.Cookie formatarCookie(Cookie cookie) {
        return SessaoUsuario.Cookie
            .builder()
            .nome(cookie.getName())
            .valor(cookie.getValue())
            .dominio(cookie.getDomain())
            .httpOnly(cookie.isHttpOnly())
            .secure(cookie.getSecure())
            .path(cookie.getPath())
            .build();
    }
}

