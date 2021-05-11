package br.com.azi.patrimoniomobiliario.integrationtest.helper;

import org.springframework.http.HttpHeaders;

import javax.servlet.http.Cookie;

public class AuthenticationHelper {

    private static final String COOKIE_AUTHENTICATION_NAME = "JSESSIONID";
    private static final String COOKIE_AUTHENTICATION_VALUE = "fabe62ad-7d07-4437-bf3f-5d5458240b58";

    public static HttpHeaders getHeaders() {
        return mockHeaders("usuario", "4", "ESTRUTURA_ORGANIZACIONAL");
    }

    public static HttpHeaders mockHeaders(String usuario, String domainId, String domainType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("az-user", usuario);
        httpHeaders.add("az-domain-id", domainId);
        httpHeaders.add("az-domain-type", domainType);

        return httpHeaders;
    }

    public static Cookie getCookies() {
        return new Cookie(COOKIE_AUTHENTICATION_NAME, COOKIE_AUTHENTICATION_VALUE);
    }
}
