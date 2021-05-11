package br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.uploadfile;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.exception.UploadArquivoException;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.entity.HalIntegrationProperties;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.uploadfile.converter.UploadFileIntegrationConverter;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.uploadfile.entity.UploadFileIntegrationResponse;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.CookieUtils;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.JsonUtils;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.RestUtils;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.UrlBuilder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UploadFileIntegrationUseCase {

    private static final String PATH_TO_UPLOAD = "/public/arquivos";

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private UrlBuilder urlBuilder;

    @Autowired
    private RestUtils restUtils;

    @Autowired
    private UploadFileIntegrationConverter converter;

    public Arquivo executar(HalIntegrationProperties integrationProperties, SessaoUsuario sessaoUsuario, Arquivo arquivo) {
        String url = criarUrl(integrationProperties);
        Request request = criarRequisicao(integrationProperties, sessaoUsuario, url, arquivo);

        try {
            Response response = restUtils.execute(request);
            handleStatusError(response);
            UploadFileIntegrationResponse responseEntity = converterRetorno(response);

            return converter.to(responseEntity);
        } catch (IOException e) {
            throw new UploadArquivoException("Não foi possível fazer o upload do arquivo no file manager na uri:" + arquivo.getNome(), e);
        }
    }

    private String criarUrl(HalIntegrationProperties integrationProperties) {
        return urlBuilder
            .domain(integrationProperties.getHal().getUrl())
            .path(PATH_TO_UPLOAD)
            .build();
    }

    private Request criarRequisicao(HalIntegrationProperties integrationProperties, SessaoUsuario sessaoUsuario, String url, Arquivo arquivo) {
        RequestBody requestBody = new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", this.normalizeFilename(arquivo.getNome()), RequestBody.create(MediaType.parse(arquivo.getContentType()), arquivo.getContent()))
            .addFormDataPart("repository", integrationProperties.getHal().getRepository())
            .addFormDataPart("thumbnail", "false")
            .build();

        return new Request.Builder()
            .url(url)
            .post(requestBody)
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
            throw new UploadArquivoException("Não foi possível fazer o upload do arquivo no file manager na uri");
        }
    }

    private UploadFileIntegrationResponse converterRetorno(Response response) throws IOException {
        return jsonUtils.fromJson(response.body().string(), UploadFileIntegrationResponse.class);
    }
}
