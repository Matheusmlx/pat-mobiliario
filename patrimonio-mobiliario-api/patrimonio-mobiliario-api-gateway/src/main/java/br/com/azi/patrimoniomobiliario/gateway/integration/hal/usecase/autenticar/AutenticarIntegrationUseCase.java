package br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.autenticar;


import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.exception.AlterarPropriedadeException;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.entity.HalIntegrationProperties;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.RestUtils;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.UrlBuilder;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
public class AutenticarIntegrationUseCase {

    private static final String PATH = "/auth/j_spring_security_check";

    @Autowired
    private UrlBuilder urlBuilder;

    @Autowired
    private RestUtils restUtils;

    public SessaoUsuario executar(HalIntegrationProperties integrationProperties) {
        String url = criarUrl(integrationProperties);
        Request request = criarRequisicao(url, integrationProperties);

        try {
            Response response = restUtils.execute(request);
            handleStatusError(response);

            return converterResponse(response);
        } catch (IOException e) {
            throw new AlterarPropriedadeException("Não foi possivel salvar a propriedade no hal-config", e);
        }
    }

    private String criarUrl(HalIntegrationProperties integrationProperties) {
        return urlBuilder
            .domain(integrationProperties.getHal().getUrl())
            .path(PATH)
            .build();
    }

    private Request criarRequisicao(String url, HalIntegrationProperties integrationProperties) {
        RequestBody requestBody = new FormBody.Builder()
            .add("username", integrationProperties.getUsuario().getLogin())
            .add("password", integrationProperties.getUsuario().getSenha())
            .add("uriRedirectAfterLogin", integrationProperties.getHal().getUrl())
            .add("produto", "setup")
            .add("tipo", "INTERNO")
            .build();

        return new Request
            .Builder()
            .header("Content-Type", "application/x-www-form-urlencoded")
            .post(requestBody)
            .url(url)
            .build();
    }

    private void handleStatusError(Response response) {
        if (!response.isSuccessful()) {
            throw new AlterarPropriedadeException("Não foi possivel salvar a propriedade no hal-config");
        }
    }

    private SessaoUsuario converterResponse(Response response) {
        return SessaoUsuario
            .builder()
            .cookies(Collections.singletonList(
                SessaoUsuario.Cookie
                    .builder()
                    .nome("JSESSIONID")
                    .valor(response.priorResponse().priorResponse().headers().get("Set-Cookie").substring(11, 47))
                    .build()
            ))
            .build();
    }
}
