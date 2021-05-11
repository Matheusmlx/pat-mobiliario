package br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.promotefile;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.exception.PromoteArquivoException;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.entity.HalIntegrationProperties;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.uploadfile.entity.UploadFileIntegrationResponse;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.CookieUtils;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.JsonUtils;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.RestUtils;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.UrlBuilder;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class PromoteFileIntegrationUseCase {
    private static final String PATH_TO_PROMOTE = "/public/arquivos/definitivo?uri=%s";

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private UrlBuilder urlBuilder;

    @Autowired
    private RestUtils restUtils;

    public void executar(HalIntegrationProperties integrationProperties, SessaoUsuario sessaoUsuario, Arquivo arquivo) {
        if(Objects.nonNull(arquivo.getUri())) {
            String url = criarUrl(integrationProperties, arquivo);
            Request request = criarRequisicao(sessaoUsuario, url);

            try {
                Response response = restUtils.execute(request);
                handleStatusError(response);
            } catch (IOException e) {
                throw new PromoteArquivoException("Não foi possível promover o arquivo no file manager na uri:" + arquivo.getNome(), e);
            }
        }
    }

    private String criarUrl(HalIntegrationProperties integrationProperties, Arquivo arquivo) {
        return urlBuilder
            .domain(integrationProperties.getHal().getUrl())
            .path(PATH_TO_PROMOTE)
            .params(arquivo.getUri())
            .build();
    }

    private Request criarRequisicao(SessaoUsuario sessaoUsuario, String url) {
        RequestBody requestBody = RequestBody.create(null, new byte[0]);

        return new Request.Builder()
            .url(url)
            .addHeader("Cookie", CookieUtils.getCookieAutenticacao(sessaoUsuario))
            .post(requestBody)
            .build();
    }

    private void handleStatusError(Response response) {
        if (!response.isSuccessful()) {
            throw new PromoteArquivoException("Não foi possível promover o arquivo no file manager na uri");
        }
    }

    private UploadFileIntegrationResponse converterRetorno(Response response) throws IOException {
        return jsonUtils.fromJson(response.body().string(), UploadFileIntegrationResponse.class);
    }
}
