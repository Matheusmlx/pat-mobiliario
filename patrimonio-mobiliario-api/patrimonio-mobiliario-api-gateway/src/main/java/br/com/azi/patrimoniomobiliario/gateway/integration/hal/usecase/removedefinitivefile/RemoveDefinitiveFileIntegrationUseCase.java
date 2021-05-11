package br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.removedefinitivefile;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.exception.RemocaoArquivoException;
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
public class RemoveDefinitiveFileIntegrationUseCase {

    private static final String PATH_TO_REMOVE_DEFINITIVE = "/public/arquivos?uri=%s";

    @Autowired
    private UrlBuilder urlBuilder;

    @Autowired
    private RestUtils restUtils;

    public void executar(HalIntegrationProperties integrationProperties, SessaoUsuario sessaoUsuario, Arquivo arquivo) {
        String url = criarUrl(integrationProperties, arquivo);
        Request request = criarRequisicao(sessaoUsuario, url);

        try {
            Response response = restUtils.execute(request);
            handleStatusError(response);
        } catch (IOException e) {
            throw new RemocaoArquivoException("Não foi possível remocao o arquivo definitivo na uri:" + arquivo.getUri(), e);
        }
    }

    private String criarUrl(HalIntegrationProperties integrationProperties, Arquivo arquivo) {
        return urlBuilder
            .domain(integrationProperties.getHal().getUrl())
            .path(PATH_TO_REMOVE_DEFINITIVE)
            .params(arquivo.getUri())
            .build();
    }

    private Request criarRequisicao(SessaoUsuario sessaoUsuario, String url) {
        return new Request.Builder()
            .url(url)
            .addHeader("Cookie", CookieUtils.getCookieAutenticacao(sessaoUsuario))
            .delete()
            .build();
    }

    private void handleStatusError(Response response) {
        if (!response.isSuccessful()) {
            throw new RemocaoArquivoException("Não foi possível remocao o arquivo definitivo na uri");
        }
    }

    private String getMediaType(ResponseBody body, String fileUri) {
        if (Objects.nonNull(body) && Objects.nonNull(body.contentType()))
            return body.contentType().toString();

        return br.com.azi.patrimoniomobiliario.utils.misc.MediaType.findMIMETypeBy(extractFileExtention(fileUri));
    }

    private String extractFileExtention(String fileUri) {
        return fileUri.substring(fileUri.lastIndexOf(".") + 1);
    }
}
