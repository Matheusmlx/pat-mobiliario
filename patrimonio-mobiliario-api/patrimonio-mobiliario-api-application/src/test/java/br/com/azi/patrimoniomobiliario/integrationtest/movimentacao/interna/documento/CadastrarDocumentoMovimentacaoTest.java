package br.com.azi.patrimoniomobiliario.integrationtest.movimentacao.interna.documento;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.cadastro.CadastrarDocumentoMovimentacaoInputData;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.AuthenticationHelper;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.JsonHelper;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql("/datasets/movimentacao/interna/documento/cadastrar-documentos-movimentacao.sql")
@Transactional
public class CadastrarDocumentoMovimentacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8022);

    @Test
    public void deveCadastrarDocumentoParaMovimentacao() throws Exception {
        final LocalDateTime dataDocumento = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        final CadastrarDocumentoMovimentacaoInputData inputData = CadastrarDocumentoMovimentacaoInputData.builder()
            .numero("1234")
            .valor(BigDecimal.valueOf(100))
            .uriAnexo("repo1:patrimoniomobiliario/teste.pdf")
            .data(DateUtils.asDate(dataDocumento))
            .tipo(1L)
            .build();

        createMockRequisicaoSetup();

        mockMvc.perform(
            post("/movimentacoes/internas/1/documentos")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.toJson(inputData)))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.numero", equalTo("1234")))
            .andExpect(jsonPath("$.valor", equalTo(100)))
            .andExpect(jsonPath("$.uriAnexo", equalTo("repo1:patrimoniomobiliario/teste.pdf")))
            .andExpect(jsonPath("$.data", notNullValue()))
            .andExpect(jsonPath("$.tipo", equalTo(1)))
            .andExpect(jsonPath("$.movimentacao", equalTo(1)));
    }

    private void createMockRequisicaoSetup() {
        wireMockRule.stubFor(WireMock.post(urlEqualTo("/setup/hal/public/arquivos/definitivo?uri=repo1:patrimoniomobiliario/teste.pdf"))
            .willReturn(aResponse()
                .withStatus(200)));
    }
}
