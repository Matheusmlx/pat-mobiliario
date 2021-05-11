package br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarsetoresquenaosaoalmoxarifadopororgaoefuncao;

import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.entity.HalIntegrationProperties;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarsetoresquenaosaoalmoxarifadopororgaoefuncao.converter.BuscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncaoConverter;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarsetoresquenaosaoalmoxarifadopororgaoefuncao.entity.BuscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncaoIntegrationResponse;
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
public class BuscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncaoUseCase {

    private static final String PATH_TO_BUSCA = "/unidadeOrganizacionalDominio/%s/buscarSetoresFiltrandoPorAlmoxarifado/%s";

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private RestUtils restUtils;

    @Autowired
    private BuscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncaoConverter integrationConverter;

    public List<UnidadeOrganizacional> executar(HalIntegrationProperties integrationProperties, SessaoUsuario sessaoUsuario, List<String> funcoes, Long orgaoId, Boolean almoxarifado) {
        String url = criarUrl(integrationProperties, sessaoUsuario, funcoes, orgaoId, almoxarifado);
        Request request = criarRequisicao(url, sessaoUsuario);

        try {
            Response response = restUtils.execute(request);
            handleStatusError(response);
            BuscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncaoIntegrationResponse responseEntity = converterRetorno(response);

            return integrationConverter.to(responseEntity);
        } catch (IOException e) {
            throw new BuscarUnidadesOrganizacionaisException("Não foi possivel buscarorgao listagem de estruturas organizacionais no setup.", e);
        }
    }

    private String criarUrl(HalIntegrationProperties integrationProperties, SessaoUsuario sessaoUsuario, List<String> funcoes, Long orgaoId, Boolean almoxarifado) {
        UrlBuilder urlBuilder = new UrlBuilder();

        urlBuilder
            .domain(integrationProperties.getHal().getUrl())
            .path(PATH_TO_BUSCA)
            .params(sessaoUsuario.getId().toString(), orgaoId.toString());

        funcoes.forEach(funcao -> urlBuilder.addQueryParameter("funcao=" + funcao));
        urlBuilder.addQueryParameter("almoxarifado="+almoxarifado.toString());

        return urlBuilder.build();
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
            throw new BuscarUnidadesOrganizacionaisException("Não foi possivel realizar a busca de setores");
        }
    }

    private BuscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncaoIntegrationResponse converterRetorno(Response response) throws IOException {
        BuscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncaoIntegrationResponse.EstruturaOrganizacional[] responseEntity = jsonUtils.fromJson(response.body().string(),
            BuscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncaoIntegrationResponse.EstruturaOrganizacional[].class);

        return BuscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncaoIntegrationResponse
            .builder()
            .content(Arrays.asList(responseEntity))
            .build();
    }
}
