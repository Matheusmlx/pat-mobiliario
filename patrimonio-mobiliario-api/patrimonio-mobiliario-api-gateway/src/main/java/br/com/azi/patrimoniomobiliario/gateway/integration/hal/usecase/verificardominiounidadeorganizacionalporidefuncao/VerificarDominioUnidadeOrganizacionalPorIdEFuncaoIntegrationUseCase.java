package br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.verificardominiounidadeorganizacionalporidefuncao;

import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.entity.HalIntegrationProperties;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.exception.BuscarUnidadesOrganizacionaisException;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.verificardominiounidadeorganizacionalporidefuncao.entity.VerificarDominioUnidadeOrganizacionalPorIdEFuncaoIntegrationResponse;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.CookieUtils;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.JsonUtils;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.RestUtils;
import br.com.azi.patrimoniomobiliario.gateway.integration.utils.UrlBuilder;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class VerificarDominioUnidadeOrganizacionalPorIdEFuncaoIntegrationUseCase {

    private static final String PATH_TO_BUSCA = "/unidadeOrganizacionalDominio/%s/verificarDominioOrgaoPorFuncao?unidadeOrganizacionalId=%s";

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private UrlBuilder urlBuilder;

    @Autowired
    private RestUtils restUtils;

    public Boolean executar(HalIntegrationProperties integrationProperties, SessaoUsuario sessaoUsuario, Long unidadeOrganizacionalId, List<String> funcoes) {
        String url = criarUrl(integrationProperties, sessaoUsuario.getId(), unidadeOrganizacionalId, funcoes);
        Request request = criarRequisicao(url, sessaoUsuario);

        try {
            Response response = restUtils.execute(request);
            handleStatusError(response);
            VerificarDominioUnidadeOrganizacionalPorIdEFuncaoIntegrationResponse responseEntity = converterRetorno(response);

            return responseEntity.getPossuiDominio();
        } catch (IOException e) {
            throw new BuscarUnidadesOrganizacionaisException("Não foi possivel buscarorgao listagem de estruturas organizacionais no setup.", e);
        }
    }

    private String criarUrl(HalIntegrationProperties integrationProperties, Long usuarioId, Long unidadeOrganizacionalId, List<String> funcoes) {
        String path = PATH_TO_BUSCA;

        for (String funcao : funcoes) {
            path = String.format("%s&%s=%s", path, "funcao", funcao);
        }

        return urlBuilder
            .domain(integrationProperties.getHal().getUrl())
            .path(path)
            .params(usuarioId.toString(), unidadeOrganizacionalId.toString())
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

    private VerificarDominioUnidadeOrganizacionalPorIdEFuncaoIntegrationResponse converterRetorno(Response response) throws IOException {
        return jsonUtils.fromJson(response.body().string(), VerificarDominioUnidadeOrganizacionalPorIdEFuncaoIntegrationResponse.class);
    }
}
