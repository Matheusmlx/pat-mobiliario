package br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarunidadesorganizacionais;


import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.entity.HalIntegrationProperties;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarunidadesorganizacionais.converter.BuscarUnidadesOrganizacionaisIntegrationConverter;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarunidadesorganizacionais.entity.BuscarUnidadesOrganizacionaisIntegrationResponse;
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
public class BuscarUnidadesOrganizacionaisIntegrationUseCase {

    private static final String PATH_TO_BUSCA = "/unidadeOrganizacionalDominio/%s/buscarOrgaos";

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private UrlBuilder urlBuilder;

    @Autowired
    private RestUtils restUtils;

    @Autowired
    private BuscarUnidadesOrganizacionaisIntegrationConverter unidadesorganizacionaisIntegrationConverter;

    public List<UnidadeOrganizacional> executar(HalIntegrationProperties integrationProperties, SessaoUsuario sessaoUsuario) {
        String url = criarUrl(integrationProperties, sessaoUsuario);
        Request request = criarRequisicao(url, sessaoUsuario);

        try {
            Response response = restUtils.execute(request);
            handleStatusError(response);
            BuscarUnidadesOrganizacionaisIntegrationResponse responseEntity = converterRetorno(response);

            return unidadesorganizacionaisIntegrationConverter.to(responseEntity);
        } catch (IOException e) {
            throw new BuscarUnidadesOrganizacionaisException("Não foi possivel buscarorgao listagem de estruturas organizacionais no setup.", e);
        }
    }

    private String criarUrl(HalIntegrationProperties integrationProperties, SessaoUsuario sessaoUsuario) {
        return urlBuilder
            .domain(integrationProperties.getHal().getUrl())
            .path(PATH_TO_BUSCA)
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

    private BuscarUnidadesOrganizacionaisIntegrationResponse converterRetorno(Response response) throws IOException {
        BuscarUnidadesOrganizacionaisIntegrationResponse.EstruturaOrganizacional[] responseEntity = jsonUtils.fromJson(response.body().string(), BuscarUnidadesOrganizacionaisIntegrationResponse.EstruturaOrganizacional[].class);

        return BuscarUnidadesOrganizacionaisIntegrationResponse
            .builder()
            .content(Arrays.asList(responseEntity))
            .build();
    }
}
