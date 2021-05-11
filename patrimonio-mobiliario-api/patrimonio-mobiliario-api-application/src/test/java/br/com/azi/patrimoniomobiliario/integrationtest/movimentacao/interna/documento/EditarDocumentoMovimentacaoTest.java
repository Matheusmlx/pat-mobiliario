package br.com.azi.patrimoniomobiliario.integrationtest.movimentacao.interna.documento;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.edicao.EditarDocumentoMovimentacaoInputData;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.AuthenticationHelper;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql("/datasets/movimentacao/interna/documento/editar-documentos-movimentacao.sql")
@Transactional
public class EditarDocumentoMovimentacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8022);

    @Test
    public void deveEditarDocumentoDaMovimentacao() throws Exception {
        final EditarDocumentoMovimentacaoInputData inputData = EditarDocumentoMovimentacaoInputData.builder()
            .tipo(2L)
            .numero("1111")
            .uriAnexo("repo1:patrimoniomobiliario/teste2.pdf")
            .valor(new BigDecimal(1200))
            .data(new Date())
            .build();

        createMockRequisicaoSetup();

        mockMvc.perform(
            put("/movimentacoes/internas/2/documentos/1")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.toJson(inputData)))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.numero", equalTo(inputData.getNumero())))
            .andExpect(jsonPath("$.valor", equalTo(1200)))
            .andExpect(jsonPath("$.uriAnexo", equalTo(inputData.getUriAnexo())))
            .andExpect(jsonPath("$.data", notNullValue()))
            .andExpect(jsonPath("$.tipo", equalTo(inputData.getTipo().intValue())))
            .andExpect(jsonPath("$.movimentacao", equalTo(2)));
    }

    private void createMockRequisicaoSetup() {
        wireMockRule.stubFor(WireMock.post(urlEqualTo("/setup/hal/public/arquivos/definitivo?uri=repo1:patrimoniomobiliario/teste2.pdf"))
            .willReturn(aResponse()
                .withStatus(200)));

        wireMockRule.stubFor(WireMock.delete(urlEqualTo("/setup/hal/public/arquivos?uri=repo1:patrimoniomobiliario/teste1.pdf"))
            .willReturn(aResponse()
                .withStatus(200)));
    }
}
