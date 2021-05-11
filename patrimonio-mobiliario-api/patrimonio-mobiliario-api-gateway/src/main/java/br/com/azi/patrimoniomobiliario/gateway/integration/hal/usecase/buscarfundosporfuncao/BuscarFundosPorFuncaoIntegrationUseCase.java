package br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarfundosporfuncao;


import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.entity.HalIntegrationProperties;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarfundosporfuncao.converter.BuscarFundosPorFuncaoIntegrationConverter;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarfundosporfuncao.entity.BuscarFundosPorFuncaoIntegrationResponse;
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
public class BuscarFundosPorFuncaoIntegrationUseCase {

    private static final String PATH_TO_BUSCA = "/unidadeOrganizacionalDominio/%s/buscarFundosPorEstruturaAdministradoraEFuncao?unidadeOrganizacionalId=%s";

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private UrlBuilder urlBuilder;

    @Autowired
    private RestUtils restUtils;

    @Autowired
    private BuscarFundosPorFuncaoIntegrationConverter integrationConverter;

    public List<UnidadeOrganizacional> executar(HalIntegrationProperties integrationProperties, SessaoUsuario sessaoUsuario, List<String> funcoes, Long estruturaAdministradoraId) {
        String url = criarUrl(integrationProperties, sessaoUsuario, funcoes, estruturaAdministradoraId);
        Request request = criarRequisicao(url, sessaoUsuario);

        try {
            Response response = restUtils.execute(request);
            handleStatusError(response);
            BuscarFundosPorFuncaoIntegrationResponse responseEntity = converterRetorno(response);

            return integrationConverter.to(responseEntity);
        } catch (IOException e) {
            throw new BuscarUnidadesOrganizacionaisException("Não foi possível encontrar fundos.", e);
        }
    }

    private String criarUrl(HalIntegrationProperties integrationProperties, SessaoUsuario sessaoUsuario, List<String> funcoes, Long estruturaAdministradoraId) {
        String path = PATH_TO_BUSCA;

        for (String funcao : funcoes) {
                path = String.format("%s&%s=%s", path, "funcao", funcao);
        }

        return urlBuilder
            .domain(integrationProperties.getHal().getUrl())
            .path(path)
            .params(sessaoUsuario.getId().toString(), estruturaAdministradoraId.toString())
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

    private BuscarFundosPorFuncaoIntegrationResponse converterRetorno(Response response) throws IOException {
        BuscarFundosPorFuncaoIntegrationResponse.EstruturaOrganizacional[] responseEntity = jsonUtils.fromJson(response.body().string(), BuscarFundosPorFuncaoIntegrationResponse.EstruturaOrganizacional[].class);

        return BuscarFundosPorFuncaoIntegrationResponse
            .builder()
            .content(Arrays.asList(responseEntity))
            .build();
    }
}
