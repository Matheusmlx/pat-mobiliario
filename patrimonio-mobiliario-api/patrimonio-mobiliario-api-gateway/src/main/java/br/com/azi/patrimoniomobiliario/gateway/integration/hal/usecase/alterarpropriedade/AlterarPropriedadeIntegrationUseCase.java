package br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.alterarpropriedade;

import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.exception.AlterarPropriedadeException;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.entity.HalIntegrationProperties;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.alterarpropriedade.entity.AlterarPropriedadeIntegrationRequest;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.CookieUtils;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.JsonUtils;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.RestUtils;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.UrlBuilder;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AlterarPropriedadeIntegrationUseCase {

    private static final String PATH = "/parametros/simple/patrimonio-mobiliario";

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private UrlBuilder urlBuilder;

    @Autowired
    private RestUtils restUtils;

    public void executar(HalIntegrationProperties integrationProperties, SessaoUsuario sessaoUsuario, String nome, String valor) {
        String url = criarUrl(integrationProperties);
        AlterarPropriedadeIntegrationRequest requestBody = criarBodyRequest(nome, valor);
        Request request = criarRequisicao(url, requestBody, sessaoUsuario);

        try {
            Response response = restUtils.execute(request);
            handleStatusError(response);
        } catch (IOException e) {
            throw new AlterarPropriedadeException("Não foi possivel salvar a propriedade no hal-config", e);
        }
    }

    private String criarUrl(HalIntegrationProperties integrationProperties) {
        return urlBuilder
            .domain(integrationProperties.getHalConfig().getUrl())
            .path(PATH)
            .build();
    }

    private AlterarPropriedadeIntegrationRequest criarBodyRequest(String nome, String valor) {
        return AlterarPropriedadeIntegrationRequest
            .builder()
            .key(nome)
            .value(valor)
            .build();
    }

    private Request criarRequisicao(String url, AlterarPropriedadeIntegrationRequest requestBody, SessaoUsuario sessaoUsuario) {
        return new Request
            .Builder()
            .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonUtils.toJson(requestBody)))
            .addHeader("Cookie", CookieUtils.getCookieAutenticacao(sessaoUsuario))
            .url(url)
            .build();
    }

    private void handleStatusError(Response response) {
        if (!response.isSuccessful()) {
            final String mensagem = "[" + response.code() + "] - " + response.message() ;
            throw new AlterarPropriedadeException("Não foi possivel salvar a propriedade no hal-config. " + mensagem);
        }
    }
}
