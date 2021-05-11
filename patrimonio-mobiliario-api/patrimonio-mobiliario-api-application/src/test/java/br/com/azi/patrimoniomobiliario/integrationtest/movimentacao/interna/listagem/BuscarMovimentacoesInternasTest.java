package br.com.azi.patrimoniomobiliario.integrationtest.movimentacao.interna.listagem;

import br.com.azi.patrimoniomobiliario.integrationtest.helper.AuthenticationHelper;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.FileHelper;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BuscarMovimentacoesInternasTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8022);

    @Test
    @Sql({"/datasets/usuario.sql", "/datasets/movimentacao/interna/buscar-movimentacoes-internas.sql"})
    public void deveBuscarMovimentacoesInternas() throws Exception {

        createMockRequisicaoSetup();

        mockMvc.perform(get("/movimentacoes/internas?page=1&size=10&direction=ASC&sort=situacao")
            .headers(AuthenticationHelper.getHeaders())
            .cookie(AuthenticationHelper.getCookies())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", Matchers.equalTo(17)))
            .andExpect(jsonPath("$.items[0].codigo", Matchers.equalTo("00017")))
            .andExpect(jsonPath("$.items[0].tipo", Matchers.equalTo("DISTRIBUICAO")))
            .andExpect(jsonPath("$.items[0].orgao", Matchers.equalTo("BPLOB")))
            .andExpect(jsonPath("$.items[0].setorOrigem", Matchers.equalTo("ASCOM - Assessoria de Comunicação Social")))
            .andExpect(jsonPath("$.items[0].setorDestino", Matchers.equalTo("ASTI - Assessoria de Tecnologia em Informação")))
            .andExpect(jsonPath("$.items[0].situacao", Matchers.equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.items[1].id", Matchers.equalTo(19)))
            .andExpect(jsonPath("$.items[1].codigo", Matchers.equalTo("00019")))
            .andExpect(jsonPath("$.items[1].tipo", Matchers.equalTo("DISTRIBUICAO")))
            .andExpect(jsonPath("$.items[1].orgao", Matchers.equalTo("COB")))
            .andExpect(jsonPath("$.items[1].setorOrigem", Matchers.equalTo("CONDIR - Conselho Diretor")))
            .andExpect(jsonPath("$.items[1].setorDestino", Matchers.equalTo("CONSAD - Conselho Administrativo")))
            .andExpect(jsonPath("$.items[1].motivoObservacao", Matchers.equalTo("Motivo Estorno")))
            .andExpect(jsonPath("$.items[1].numeroNotaLancamentoContabil", Matchers.equalTo("2342NL342342")))
            .andExpect(jsonPath("$.items[1].dataNotaLancamentoContabil", Matchers.equalTo(DateValidate.formatarData(LocalDateTime.of(2021, 1, 4, 0,0, 0)))))
            .andExpect(jsonPath("$.items[1].numeroProcesso", Matchers.equalTo("351351")))
            .andExpect(jsonPath("$.items[1].situacao", Matchers.equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.totalElements", Matchers.equalTo(2)))
            .andExpect(jsonPath("$.totalPages", Matchers.equalTo(1)));
    }

    private void createMockRequisicaoSetup() {
        String response = FileHelper.getJson("setup", "unidade-organizacionais.json");

        wireMockRule.stubFor(WireMock.get(urlEqualTo("/setup/hal/unidadeOrganizacionalDominio/2/buscarOrgaosPorFuncao?funcao=Mobiliario.Normal&funcao=Mobiliario.Consulta"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(response)));

        wireMockRule.stubFor(WireMock.get(urlMatching("/setup/hal/unidadeOrganizacionalDominio/2/buscarTodasUnidadesOrganizacionaisHerdeiras.*"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(response)));

        wireMockRule.stubFor(WireMock.get(urlMatching("/setup/hal/unidadeOrganizacionalDominio/2/buscarSetoresDoOrgaoQueNaoSaoAlmoxarifado/.*?funcao=Mobiliario.Consulta"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(response)));
    }

}
