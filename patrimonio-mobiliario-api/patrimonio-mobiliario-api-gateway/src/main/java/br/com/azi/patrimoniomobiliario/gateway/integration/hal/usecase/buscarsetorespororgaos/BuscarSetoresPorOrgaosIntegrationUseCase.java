package br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarsetorespororgaos;

import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.entity.HalIntegrationProperties;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarsetorespororgaos.converter.BuscarSetoresPorOrgaosIntegrationConverter;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarsetorespororgaos.entity.BuscarSetoresPorOrgaosResponse;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class BuscarSetoresPorOrgaosIntegrationUseCase {

    private static final String PATH_TO_BUSCA = "/unidadeOrganizacionalDominio/%s/buscarTodasUnidadesOrganizacionaisHerdeiras?unidadeOrganizacionalId=%s";

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private UrlBuilder urlBuilder;

    @Autowired
    private RestUtils restUtils;

    @Autowired
    private BuscarSetoresPorOrgaosIntegrationConverter converter;

    public List<UnidadeOrganizacional> executar(HalIntegrationProperties integrationProperties, SessaoUsuario sessaoUsuario, List<Long> orgaos) {
        List<UnidadeOrganizacional> setores = new ArrayList<>();

        for(Long orgao: orgaos) {
            String url = criarUrl(integrationProperties, sessaoUsuario, orgao);
            Request request = criarRequisicao(url, sessaoUsuario);

            try {
                Response response = restUtils.execute(request);
                handleStatusError(response);
                BuscarSetoresPorOrgaosResponse responseEntity = converterRetorno(response);

                setores.addAll(converter.to(responseEntity));
            } catch (IOException e) {
                throw new BuscarUnidadesOrganizacionaisException("Não foi possivel realizar buscarorgao listagem de unidades organizacionais no setup.", e);
            }
        }

        return setores;
    }

    private String criarUrl(HalIntegrationProperties integrationProperties, SessaoUsuario sessaoUsuario, Long orgao) {
        return urlBuilder
            .domain(integrationProperties.getHal().getUrl())
            .path(PATH_TO_BUSCA)
            .params(sessaoUsuario.getId().toString(), orgao.toString())
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
            throw new BuscarUnidadesOrganizacionaisException("Não foi possivel realizar buscarlistagem de unidades organizacionais no setup");
        }
    }

    private BuscarSetoresPorOrgaosResponse converterRetorno(Response response) throws IOException {
        BuscarSetoresPorOrgaosResponse.EstruturaOrganizacional[] responseEntity = jsonUtils.fromJson(response.body().string(), BuscarSetoresPorOrgaosResponse.EstruturaOrganizacional[].class);

        return BuscarSetoresPorOrgaosResponse
            .builder()
            .content(Arrays.asList(responseEntity))
            .build();
    }
}
