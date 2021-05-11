package br.com.azi.patrimoniomobiliario.integrationtest.movimentacao.interna.documento;

import br.com.azi.patrimoniomobiliario.integrationtest.helper.AuthenticationHelper;
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
import org.springframework.transaction.annotation.Transactional;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RemoverDocumentoMovimentacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8022);

    @Test
    @Rollback
    @Sql("/datasets/movimentacao/interna/documento/remover-documento-movimentacao.sql")
    @Transactional
    public void deveRemoverDocumento() throws Exception {
        createMockRequisicaoSetup();
        mockMvc.perform(
            delete("/movimentacoes/internas/20/documentos/1")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful());
    }

    private void createMockRequisicaoSetup() {
        wireMockRule.stubFor(WireMock.delete(urlEqualTo("/setup/hal/public/arquivos?uri=repo1:patrimoniomobiliario/teste.pdf"))
            .willReturn(aResponse()
                .withStatus(200)));
    }
}
