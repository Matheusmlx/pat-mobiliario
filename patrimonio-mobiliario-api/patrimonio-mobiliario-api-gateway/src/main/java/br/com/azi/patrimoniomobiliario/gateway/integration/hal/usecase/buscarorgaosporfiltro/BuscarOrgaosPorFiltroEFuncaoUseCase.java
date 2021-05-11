package br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarorgaosporfiltro;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.entity.HalIntegrationProperties;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarorgaosporfiltro.converter.BuscarOrgaosPorFuncaoPaginadoIntegrationConverter;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarorgaosporfiltro.entity.BuscarOrgaosPorFuncaoPaginadoIntegrationResponse;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.exception.BuscarUnidadesOrganizacionaisException;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.CookieUtils;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.JsonUtils;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.RestUtils;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.UrlBuilder;
import lombok.RequiredArgsConstructor;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class BuscarOrgaosPorFiltroEFuncaoUseCase {

    private static final String PATH_TO_BUSCA = "/unidadeOrganizacionalDominio/%s/buscarOrgaosPorFuncaoPaginado";

    private final JsonUtils jsonUtils;

    private final UrlBuilder urlBuilder;

    private final RestUtils restUtils;

    private final BuscarOrgaosPorFuncaoPaginadoIntegrationConverter integrationConverter;

    public ListaPaginada<UnidadeOrganizacional> executar(HalIntegrationProperties integrationProperties, SessaoUsuario sessaoUsuario, List<String> funcoes, String siglaNome, long page, long size) {
        String url = criarUrl(integrationProperties, sessaoUsuario, funcoes, siglaNome, page, size);
        Request request = criarRequisicao(url, sessaoUsuario);

        try {
            Response response = restUtils.execute(request);
            handleStatusError(response);
            BuscarOrgaosPorFuncaoPaginadoIntegrationResponse responseEntity = converterRetorno(response);

            return integrationConverter.to(responseEntity);
        } catch (IOException e) {
            throw new BuscarUnidadesOrganizacionaisException("Não foi possivel buscarorgao listagem de estruturas organizacionais no setup.", e);
        }
    }

    private String criarUrl(HalIntegrationProperties integrationProperties, SessaoUsuario sessaoUsuario, List<String> funcoes, String siglaNome, long page, long size) {
        String path = String.format("%s?page=%s&size=%s", PATH_TO_BUSCA, page, size);

        for (String funcao : funcoes) {
            path = String.format("%s&%s=%s", path, "funcao", funcao);
        }

        if (Objects.isNull(siglaNome)) {
            siglaNome = "";
        }

        path = String.format("%s&%s=%s", path, "siglaNome", siglaNome);

        return urlBuilder
            .domain(integrationProperties.getHal().getUrl())
            .path(path)
            .params(sessaoUsuario.getId().toString())
            .build();
    }

    private Request criarRequisicao(String url, SessaoUsuario sessaoUsuario) {
        return new Request.Builder()
            .get()
            .addHeader("Cookie", CookieUtils.getCookieAutenticacao(sessaoUsuario))
            .url(url)
            .build();
    }

    private void handleStatusError(Response response) {
        if (!response.isSuccessful()) {
            throw new BuscarUnidadesOrganizacionaisException("Não foi possivel realizar a busca de unidades organizacionais no setup");
        }
    }

    private BuscarOrgaosPorFuncaoPaginadoIntegrationResponse converterRetorno(Response response) throws IOException {
        return jsonUtils.fromJson(
            response.body().string(),
            BuscarOrgaosPorFuncaoPaginadoIntegrationResponse.class
        );
    }
}
