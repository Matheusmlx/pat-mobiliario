package br.com.azi.patrimoniomobiliario.gateway.integration.utils;

import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.exception.UsuarioNaoAutenticadoException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CookieUtils {

    private static final String COOKIE_AUTENTICACAO_NOME = "JSESSIONID";

    public static String getCookieAutenticacao(SessaoUsuario sessaoUsuario) {
        Optional<SessaoUsuario.Cookie> cookieAutenticacao = sessaoUsuario.getCookies()
            .stream()
            .filter(cookie -> COOKIE_AUTENTICACAO_NOME.equals(cookie.getNome()))
            .findFirst();

        SessaoUsuario.Cookie cookieEncontrado = cookieAutenticacao.orElseThrow(() -> new UsuarioNaoAutenticadoException("Usuario não autenticado."));

        return String.format("%s=%s", cookieEncontrado.getNome(), cookieEncontrado.getValor());
    }

}
