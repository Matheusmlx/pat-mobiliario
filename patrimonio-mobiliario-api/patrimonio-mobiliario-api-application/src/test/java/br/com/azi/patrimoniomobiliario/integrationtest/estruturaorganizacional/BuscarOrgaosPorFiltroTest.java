package br.com.azi.patrimoniomobiliario.integrationtest.estruturaorganizacional;

import br.com.azi.patrimoniomobiliario.integrationtest.helper.AuthenticationHelper;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.FileHelper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql({"/datasets/usuario.sql"})
public class BuscarOrgaosPorFiltroTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8022);

    @Test
    @Rollback
    @Transactional
    public void deveBuscaOrgaosNoSetupComPaginacaoCorreta() throws Exception{

        createMockRequisicaoSetup("/setup/hal/unidadeOrganizacionalDominio/2/buscarOrgaosPorFuncaoPaginado?page=0&size=10&funcao=Mobiliario.Normal&funcao=Mobiliario.Consulta&siglaNome=");

        mockMvc.perform(
            get("/unidades-organizacionais/orgaos?page=1&size=10")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items", hasSize(4)))
            .andExpect(jsonPath("$.totalElements", equalTo(4)))
            .andExpect(jsonPath("$.totalPages", equalTo(1)));
    }

    @Test
    @Rollback
    @Transactional
    public void deveBuscaOrgaosNoSetupComFiltroCorreto() throws Exception{

        createMockRequisicaoSetup("/setup/hal/unidadeOrganizacionalDominio/2/buscarOrgaosPorFuncaoPaginado?page=0&size=10&funcao=Mobiliario.Normal&funcao=Mobiliario.Consulta&siglaNome=ORG");

        mockMvc.perform(
            get("/unidades-organizacionais/orgaos?page=1&size=10&conteudo=ORG")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items", hasSize(4)))
            .andExpect(jsonPath("$.totalElements", equalTo(4)))
            .andExpect(jsonPath("$.totalPages", equalTo(1)));
    }

    private void createMockRequisicaoSetup(String url) {
        String response = FileHelper.getJson("setup", "orgaos-paginado.json");

        wireMockRule.stubFor(WireMock.get(urlEqualTo(url))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(response)));
    }

}
