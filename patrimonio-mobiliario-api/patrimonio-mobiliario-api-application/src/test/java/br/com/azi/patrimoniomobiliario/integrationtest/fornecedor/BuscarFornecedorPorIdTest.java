package br.com.azi.patrimoniomobiliario.integrationtest.fornecedor;

import br.com.azi.patrimoniomobiliario.domain.usecase.fornecedor.buscarporid.BuscarFornecedorPorIdInputData;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.AuthenticationHelper;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.FileHelper;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.JsonHelper;
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
public class BuscarFornecedorPorIdTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8023);

    @Test
    @Rollback
    @Sql({"/datasets/usuario.sql"})
    @Transactional
    public void deveBuscarFornecedores() throws Exception{

        createMockRequisicaoEfornecedor();

        BuscarFornecedorPorIdInputData inputData = BuscarFornecedorPorIdInputData
            .builder()
            .id(1L)
            .build();

        mockMvc.perform(
            get("/fornecedores/1")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id", equalTo(1)));
    }

    private void createMockRequisicaoEfornecedor() {
        String response = FileHelper.getJson("efornecedor", "fornecedor.json");

        wireMockRule.stubFor(WireMock.get(urlEqualTo("/efornecedor/efcaz-api/fornecedores/portal/1/resumo"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(response)));
    }
}
