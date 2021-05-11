package br.com.azi.patrimoniomobiliario.integrationtest.incorporacao.documento;


import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.editar.EditarDocumentoInputData;
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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EditarDocumentoTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8022);

    @Test
    @Rollback
    @Sql({"/datasets/incorporacao/documento/documentos.sql"})
    @Transactional
    public void deveEditarDocumento() throws Exception{
        createMockRequisicao();

        EditarDocumentoInputData inputData = EditarDocumentoInputData
            .builder()
            .data(Date.from(LocalDateTime.of(2020, 7, 10, 0, 0).toInstant(ZoneOffset.UTC)))
            .incorporacao(1L)
            .tipo(1L)
            .numero("123456")
            .valor(BigDecimal.valueOf(200))
            .uriAnexo("repo1:patrimoniomobiliario/teste.pdf")
            .build();


        mockMvc.perform(
            put("/patrimonios/incorporacao/1/documentos/1")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andDo(print())
            .andExpect(jsonPath("$.numero", equalTo("123456")))
            .andExpect(jsonPath("$.data", equalTo("2020-07-09T20:00:00")))
            .andExpect(jsonPath("$.valor", equalTo(200)));

    }

    private void createMockRequisicao() {
        wireMockRule.stubFor(WireMock.post(urlEqualTo("/setup/hal/public/arquivos/definitivo?uri=repo1:patrimoniomobiliario/teste.pdf"))
            .willReturn(aResponse()
                .withStatus(200)));
    }
}
