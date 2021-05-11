package br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.downloadfile;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.exception.DownloadArquivoException;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.entity.HalIntegrationProperties;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.CookieUtils;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.RestUtils;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.UrlBuilder;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class DownloadFileIntegrationUseCase {

    private static final String PATH_TO_DOWNLOAD = "/public/arquivos?uri=%s&thumbnail=false";

    @Autowired
    private UrlBuilder urlBuilder;

    @Autowired
    private RestUtils restUtils;

    public Arquivo executar(HalIntegrationProperties integrationProperties, SessaoUsuario sessaoUsuario, String fileUri) {
        String url = criarUrl(integrationProperties, fileUri);
        Request request = criarRequisicao(sessaoUsuario, url);

        try {
            Response response = restUtils.execute(request);
            handleStatusError(response);
            ResponseBody responseBody = response.body();

            return Arquivo
                .builder()
                .contentType(getMediaType(responseBody, fileUri))
                .content(Objects.requireNonNull(responseBody).bytes())
                .build();
        } catch (IOException e) {
            throw new DownloadArquivoException("Não foi possível ler o arquivo do file manager na uri:" + fileUri, e);
        }
    }

    private String criarUrl(HalIntegrationProperties integrationProperties, String fileUri) {
        return urlBuilder
            .domain(integrationProperties.getHal().getUrl())
            .path(PATH_TO_DOWNLOAD)
            .params(fileUri)
            .build();
    }

    private Request criarRequisicao(SessaoUsuario sessaoUsuario, String url) {
        return new Request.Builder()
            .url(url)
            .addHeader("Cookie", CookieUtils.getCookieAutenticacao(sessaoUsuario))
            .get()
            .build();
    }

    private void handleStatusError(Response response) {
        if (!response.isSuccessful()) {
            throw new DownloadArquivoException("Relatório sem logo. Por favor, insira uma logo no setup e tente novamente.");
        }
    }

    private String getMediaType(ResponseBody body, String fileUri) {
        if (Objects.nonNull(body) && Objects.nonNull(body.contentType()))
            return Objects.requireNonNull(body.contentType()).toString();

        return br.com.azi.patrimoniomobiliario.utils.misc.MediaType.findMIMETypeBy(extractFileExtention(fileUri));
    }

    private String extractFileExtention(String fileUri) {
        return fileUri.substring(fileUri.lastIndexOf(".") + 1);
    }
}
