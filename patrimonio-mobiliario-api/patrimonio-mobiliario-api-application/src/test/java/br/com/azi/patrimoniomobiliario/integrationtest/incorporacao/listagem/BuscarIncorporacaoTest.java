package br.com.azi.patrimoniomobiliario.integrationtest.incorporacao.listagem;

import br.com.azi.patrimoniomobiliario.integrationtest.helper.AuthenticationHelper;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.FileHelper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
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
import org.springframework.transaction.annotation.Transactional;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Sql({"/datasets/usuario.sql", "/datasets/incorporacao/buscar-listagem-incorporacoes.sql"})
@Transactional
public class BuscarIncorporacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8022);

    @Before
    public void beforeTest() {
        createMockRequisicaoHal();
    }

    @Test
    public void deveBuscarIncorporacoesSortIdASC () throws Exception{
        mockMvc.perform(
            get("/patrimonios/incorporacao?page=1&size=10&sort=id&direction=ASC")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id",equalTo(2)))
            .andExpect(jsonPath("$.items[1].id",equalTo(3)))
            .andExpect(jsonPath("$.items[1].situacao",equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.items[2].id",equalTo(1)))
            .andExpect(jsonPath("$.items[2].situacao",equalTo("FINALIZADO")))
            .andExpect(jsonPath("$.items[3].id",equalTo(4)))
            .andExpect(jsonPath("$.items[4].id",equalTo(5)))
            .andExpect(jsonPath("$.totalElements", equalTo(5)));
    }

    @Test
    public void deveBuscarIncorporacoesSortIdDESC () throws Exception{
        mockMvc.perform(
            get("/patrimonios/incorporacao?page=1&size=10&sort=id&direction=DESC")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id",equalTo(3)))
            .andExpect(jsonPath("$.items[1].id",equalTo(2)))
            .andExpect(jsonPath("$.items[2].id",equalTo(5)))
            .andExpect(jsonPath("$.items[3].id",equalTo(4)))
            .andExpect(jsonPath("$.items[4].id",equalTo(1)))
            .andExpect(jsonPath("$.items[0].situacao",equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.items[1].situacao",equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.items[2].situacao",equalTo("FINALIZADO")))
            .andExpect(jsonPath("$.items[3].situacao",equalTo("FINALIZADO")))
            .andExpect(jsonPath("$.items[4].situacao",equalTo("FINALIZADO")))
            .andExpect(jsonPath("$.totalElements", equalTo(5)));
    }

    @Test
    public void deveBuscarIncorporacaoSemFornecedorSortSituacao () throws Exception{
        mockMvc.perform(
            get("/patrimonios/incorporacao?conteudo=123654&page=1&size=10&sort=situacao&direction=DESC")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id",equalTo(3)))
            .andExpect(jsonPath("$.items[0].situacao",equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.totalElements", equalTo(1)));
    }

    @Test
    public void deveBuscarIncorporacaoSemFornecedorSortId () throws Exception{
        mockMvc.perform(
            get("/patrimonios/incorporacao?conteudo=123654&page=1&size=10&sort=id&direction=DESC")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id",equalTo(3)))
            .andExpect(jsonPath("$.items[0].situacao",equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.totalElements", equalTo(1)));
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
