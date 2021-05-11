package br.com.azi.patrimoniomobiliario.integrationtest.incorporacao.patrimonio;

import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemincorporados.BuscarPatrimoniosIncorporadosOutputData;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.AuthenticationHelper;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.FileHelper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback
@AutoConfigureMockMvc
@Sql({"/datasets/incorporacao/patrimonio/buscar-listagem-patrimonios-incorporados.sql"})
@Transactional
public class BuscarPatrimoniosIncorporadosTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8022);

    @Before
    public void beforeTest() {
        createMockRequisicaoHal();
    }

    private final Gson gson = new Gson();

    @Test
    public void deveBuscarPatrimoniosIncorporadosAtivosEEstornados() throws Exception {
        final int quantidadeItensRetornados = 5;
        final int quantidadeItensComSituacaoNull = 0;

        final String response = mockMvc.perform(
            get("/patrimonios/patrimonios?page=1&size=10&direction=ASC")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items.size()", equalTo(quantidadeItensRetornados)))
            .andDo(print())
            .andReturn()
            .getResponse().getContentAsString();

        final BuscarPatrimoniosIncorporadosOutputData outputData = gson.fromJson(response, BuscarPatrimoniosIncorporadosOutputData.class);
        assertEquals(outputData.getItems().stream().filter(patrimonio -> patrimonio.getSituacao() == null).count(), quantidadeItensComSituacaoNull);
    }

    @Test
    public void deveBuscarPatrimoniosIncorporadosAtivosEEstornadosPorFiltroNumeroPatrimonio() throws Exception {
        final int quantidadeItensRetornados = 1;
        final String numeroPatrimonio = "2398472894";

        mockMvc.perform(
            get("/patrimonios/patrimonios?page=1&size=10&direction=ASC")
                .param("conteudo", numeroPatrimonio)
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items.size()", equalTo(quantidadeItensRetornados)))
            .andDo(print())
            .andReturn()
            .getResponse().getContentAsString();
    }

    @Test
    public void deveBuscarPatrimoniosIncorporadosAtivosEEstornadosPorFiltroOrgao() throws Exception {
        final int quantidadeItensRetornados = 5;
        final String siglaOrgao = "BPLOB";
        final String nomeOrgao = "Bens Patrimoniais a Localizar e Outros Bens";

        mockMvc.perform(
            get("/patrimonios/patrimonios?page=1&size=10&direction=ASC")
                .param("conteudo", siglaOrgao)
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items.size()", equalTo(quantidadeItensRetornados)))
            .andDo(print())
            .andReturn()
            .getResponse().getContentAsString();

        mockMvc.perform(
            get("/patrimonios/patrimonios?page=1&size=10&direction=ASC")
                .param("conteudo", nomeOrgao)
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items.size()", equalTo(quantidadeItensRetornados)))
            .andDo(print())
            .andReturn()
            .getResponse().getContentAsString();
    }

    @Test
    public void deveBuscarPatrimoniosIncorporadosAtivosEEstornadosPorFiltroSetor() throws Exception {
        final int quantidadeItensRetornados = 5;
        final String siglaSetor = "BPL";
        final String nomeSetor = "Bens Patrimoniais a Localizar";

        mockMvc.perform(
            get("/patrimonios/patrimonios?page=1&size=10&direction=ASC")
                .param("conteudo", siglaSetor)
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items.size()", equalTo(quantidadeItensRetornados)))
            .andDo(print())
            .andReturn()
            .getResponse().getContentAsString();

        mockMvc.perform(
            get("/patrimonios/patrimonios?page=1&size=10&direction=ASC")
                .param("conteudo", nomeSetor)
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items.size()", equalTo(quantidadeItensRetornados)))
            .andDo(print())
            .andReturn()
            .getResponse().getContentAsString();
    }

    @Test
    public void naoDeveBuscarPatrimoniosQuePertencemAOrgaoOuSetorQueOUsuarioNaoTemAcesso() throws Exception {
        final String numeroPatrimonio = "2398472899";
        final String siglaOrgaoSemAcesso = "ST";
        final String siglaSetorSemAcesso = "SST";
        final int quantidadeItensRetornados = 0;

        mockMvc.perform(
            get("/patrimonios/patrimonios?page=1&size=10&direction=ASC")
                .param("conteudo", numeroPatrimonio)
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items.size()", equalTo(quantidadeItensRetornados)))
            .andDo(print());

        mockMvc.perform(
            get("/patrimonios/patrimonios?page=1&size=10&direction=ASC")
                .param("conteudo", siglaOrgaoSemAcesso)
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items.size()", equalTo(quantidadeItensRetornados)))
            .andDo(print());

        mockMvc.perform(
            get("/patrimonios/patrimonios?page=1&size=10&direction=ASC")
                .param("conteudo", siglaSetorSemAcesso)
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items.size()", equalTo(quantidadeItensRetornados)))
            .andDo(print());
    }

    private void createMockRequisicaoHal() {
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
    }
}
