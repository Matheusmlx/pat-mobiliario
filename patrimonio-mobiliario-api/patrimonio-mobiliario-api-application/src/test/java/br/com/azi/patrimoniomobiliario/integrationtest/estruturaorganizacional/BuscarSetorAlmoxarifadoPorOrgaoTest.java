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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BuscarSetorAlmoxarifadoPorOrgaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8022);

    @Test
    @Rollback
    @Sql({"/datasets/usuario.sql"})
    @Transactional
    public void deveBuscarUnidadeOrganizacional() throws Exception{
        createMockRequisicaoSetup();

        mockMvc.perform(
            get("/unidades-organizacionais/89/setores-almoxarifado")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items.[0].id", equalTo(5141)));
    }

    private void createMockRequisicaoSetup() {
        String response = FileHelper.getJson("setup", "unidade-organizacionais.json");

        wireMockRule.stubFor(WireMock.get(urlEqualTo("/setup/hal/unidadeOrganizacionalDominio/2/buscarSetoresFiltrandoPorAlmoxarifado/89?funcao=Mobiliario.Consulta&almoxarifado=true"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(response)));
    }

}
