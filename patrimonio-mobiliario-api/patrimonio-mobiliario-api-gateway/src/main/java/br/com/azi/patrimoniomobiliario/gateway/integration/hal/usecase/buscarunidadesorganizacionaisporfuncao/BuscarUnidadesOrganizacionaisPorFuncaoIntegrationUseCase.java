package br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarunidadesorganizacionaisporfuncao;


import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.entity.HalIntegrationProperties;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarunidadesorganizacionaisporfuncao.converter.BuscarUnidadesOrganizacionaisPorFuncaoIntegrationConverter;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarunidadesorganizacionaisporfuncao.entity.BuscarUnidadesOrganizacionaisPorFuncaoIntegrationResponse;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.exception.BuscarUnidadesOrganizacionaisException;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.CookieUtils;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.JsonUtils;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.RestUtils;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.UrlBuilder;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class BuscarUnidadesOrganizacionaisPorFuncaoIntegrationUseCase {

    private static final String PATH_TO_BUSCA = "/unidadeOrganizacionalDominio/%s/buscarOrgaosPorFuncao";

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private UrlBuilder urlBuilder;

    @Autowired
    private RestUtils restUtils;

    @Autowired
    private BuscarUnidadesOrganizacionaisPorFuncaoIntegrationConverter integrationConverter;

    public List<UnidadeOrganizacional> executar(HalIntegrationProperties integrationProperties, SessaoUsuario sessaoUsuario, List<String> funcoes) {
        String url = criarUrl(integrationProperties, sessaoUsuario, funcoes);
        Request request = criarRequisicao(url, sessaoUsuario);

        try {
            Response response = restUtils.execute(request);
            handleStatusError(response);
            BuscarUnidadesOrganizacionaisPorFuncaoIntegrationResponse responseEntity = converterRetorno(response);

            return integrationConverter.to(responseEntity);
        } catch (IOException e) {
            throw new BuscarUnidadesOrganizacionaisException("Não foi possivel buscarorgao listagem de estruturas organizacionais no setup.", e);
        }
    }

    private String criarUrl(HalIntegrationProperties integrationProperties, SessaoUsuario sessaoUsuario, List<String> funcoes) {
        String path = PATH_TO_BUSCA;
        boolean first = true;

        for (String funcao : funcoes) {
            if (first) {
                path = String.format("%s?%s=%s", path, "funcao", funcao);
                first = false;
            } else {
                path = String.format("%s&%s=%s", path, "funcao", funcao);
            }
        }

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
            throw new BuscarUnidadesOrganizacionaisException("Não foi possivel realizar buscarorgao listagem de unidades organizacionais no setup");
        }
    }

    private BuscarUnidadesOrganizacionaisPorFuncaoIntegrationResponse converterRetorno(Response response) throws IOException {
        BuscarUnidadesOrganizacionaisPorFuncaoIntegrationResponse.EstruturaOrganizacional[] responseEntity = jsonUtils.fromJson(response.body().string(), BuscarUnidadesOrganizacionaisPorFuncaoIntegrationResponse.EstruturaOrganizacional[].class);

        return BuscarUnidadesOrganizacionaisPorFuncaoIntegrationResponse
            .builder()
            .content(Arrays.asList(responseEntity))
            .build();
    }
}
