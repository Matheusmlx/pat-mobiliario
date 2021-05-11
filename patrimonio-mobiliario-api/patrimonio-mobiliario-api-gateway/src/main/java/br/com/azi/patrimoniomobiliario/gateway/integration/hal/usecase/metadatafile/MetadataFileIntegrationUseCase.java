package br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.metadatafile;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.exception.UploadArquivoException;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.entity.HalIntegrationProperties;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.metadatafile.converter.MetadataFileIntegrationConverter;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.metadatafile.entity.MetadataFileIntegrationResponse;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.CookieUtils;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.JsonUtils;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.RestUtils;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.UrlBuilder;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MetadataFileIntegrationUseCase {

    private static final String PATH_TO_METADATA = "/public/arquivos/metadados?uri=%s";

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private UrlBuilder urlBuilder;

    @Autowired
    private RestUtils restUtils;

    @Autowired
    private MetadataFileIntegrationConverter converter;

    public Arquivo executar(HalIntegrationProperties integrationProperties, SessaoUsuario sessaoUsuario, String fileUri) {
        String url = criarUrl(integrationProperties, fileUri);
        Request request = criarRequisicao(integrationProperties, sessaoUsuario, url);

        try {
            Response response = restUtils.execute(request);
            handleStatusError(response);
            MetadataFileIntegrationResponse responseEntity = converterRetorno(response);

            return converter.to(responseEntity);
        } catch (IOException e) {
            throw new UploadArquivoException("Não foi possível adquirir os metadados do arquivo no file manager na uri:" + fileUri, e);
        }
    }

    private String criarUrl(HalIntegrationProperties integrationProperties, String fileUri) {
        return urlBuilder
            .domain(integrationProperties.getHal().getUrl())
            .path(PATH_TO_METADATA)
            .params(fileUri)
            .build();
    }

    private Request criarRequisicao(HalIntegrationProperties integrationProperties, SessaoUsuario sessaoUsuario, String url) {
        return new Request.Builder()
            .url(url)
            .get()
            .addHeader("Cookie", CookieUtils.getCookieAutenticacao(sessaoUsuario))
            .build();
    }

    private String normalizeFilename(String filename) {
        return filename
            .replaceAll("\\s", "-")
            .replaceAll("[^0-9a-zA-Z\\-.]", "_");
    }

    private void handleStatusError(Response response) {
        if (!response.isSuccessful()) {
            throw new UploadArquivoException("Não foi possível adquirir os metadados do arquivo no file manager na uri");
        }
    }

    private MetadataFileIntegrationResponse converterRetorno(Response response) throws IOException {
        return jsonUtils.fromJson(response.body().string(), MetadataFileIntegrationResponse.class);
    }
}
