package br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarunidadeorganozacionalporid;

import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.entity.HalIntegrationProperties;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarunidadeorganozacionalporid.converter.BuscarUnidadeOrganizacionalPorIdIntegrationConverter;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarunidadeorganozacionalporid.entity.BuscarUnidadeOrganizacionalPorIdIntegrationResponse;
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
public class BuscarUnidadeOrganizacionalPorIdIntegrationUseCase {

    private static final String PATH_TO_BUSCA = "/unidadeOrganizacionalDominio/%s/buscarTodasUnidadesOrganizacionaisHerdeiras?unidadeOrganizacionalId=%s";

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private UrlBuilder urlBuilder;

    @Autowired
    private RestUtils restUtils;

    @Autowired
    private BuscarUnidadeOrganizacionalPorIdIntegrationConverter converter;

    public List<UnidadeOrganizacional> executar(HalIntegrationProperties integrationProperties, SessaoUsuario sessaoUsuario, Long orgaoId) {
        String url = criarUrl(integrationProperties, sessaoUsuario, orgaoId);
        Request request = criarRequisicao(url, sessaoUsuario);

        try {
            Response response = restUtils.execute(request);
            handleStatusError(response);
            BuscarUnidadeOrganizacionalPorIdIntegrationResponse responseEntity = converterRetorno(response);

            return converter.to(responseEntity);
        } catch (IOException e) {
            throw new BuscarUnidadesOrganizacionaisException("Não foi possivel realizar buscarorgao listagem de unidades organizacionais no setup.", e);
        }
    }

    private String criarUrl(HalIntegrationProperties integrationProperties, SessaoUsuario sessaoUsuario, Long orgaoId) {
        return urlBuilder
            .domain(integrationProperties.getHal().getUrl())
            .path(PATH_TO_BUSCA)
            .params(sessaoUsuario.getId().toString(), orgaoId.toString())
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

    private BuscarUnidadeOrganizacionalPorIdIntegrationResponse converterRetorno(Response response) throws IOException {
        BuscarUnidadeOrganizacionalPorIdIntegrationResponse.EstruturaOrganizacional[] responseEntity = jsonUtils.fromJson(response.body().string(), BuscarUnidadeOrganizacionalPorIdIntegrationResponse.EstruturaOrganizacional[].class);

        return BuscarUnidadeOrganizacionalPorIdIntegrationResponse
            .builder()
            .content(Arrays.asList(responseEntity))
            .build();
    }
}
