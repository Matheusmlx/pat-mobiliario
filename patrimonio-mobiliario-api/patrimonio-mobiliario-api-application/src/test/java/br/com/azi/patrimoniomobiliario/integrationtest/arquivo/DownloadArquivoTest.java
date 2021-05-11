package br.com.azi.patrimoniomobiliario.integrationtest.arquivo;

import br.com.azi.patrimoniomobiliario.domain.usecase.arquivo.download.DownloadInputData;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DownloadArquivoTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8022);

    @Test
    @Rollback
    @Transactional
    @Sql({"/datasets/usuario.sql"})
    public void deveRealizarDownloadDeArquivo() throws Exception{
        createMockRequisicaoBuscarMetadata();
        createMockRequisicaoBuscarArquivo();

        DownloadInputData inputData = DownloadInputData
            .builder()
            .uri("repo1:patrimoniomobiliario/teste.pdf")
            .build();

        mockMvc.perform(
                get("/v1/arquivos?uri=repo1:patrimoniomobiliario/teste.pdf&thumbnail=false")
                    .headers(AuthenticationHelper.getHeaders())
                    .cookie(AuthenticationHelper.getCookies())
                        .content(JsonHelper.toJson(inputData))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is2xxSuccessful());
    }

    private void createMockRequisicaoBuscarMetadata() {
        String response = FileHelper.getJson("setup", "download-arquivo-buscar-matadata.json");

        wireMockRule.stubFor(WireMock.get(urlEqualTo("/setup/hal/public/arquivos/metadados?uri=repo1:patrimoniomobiliario/teste.pdf"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(response)));
    }

    private void createMockRequisicaoBuscarArquivo() {
        wireMockRule.stubFor(WireMock.get(urlEqualTo("/setup/hal/public/arquivos?uri=repo1:patrimoniomobiliario/teste.pdf&thumbnail=false"))
            .willReturn(aResponse()
                .withStatus(200)));
    }
}
