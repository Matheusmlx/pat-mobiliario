package br.com.azi.patrimoniomobiliario.integrationtest.incorporacao.documento;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.CadastrarDocumentoInputData;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CadastrarDocumentoTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8022);

    @Test
    @Rollback
    @Sql({"/datasets/incorporacao/documento/documentos.sql"})
    @Transactional
    public void deveCadastrarDocumento() throws Exception{

        createMockRequisicaoSetup();

        CadastrarDocumentoInputData inputData = CadastrarDocumentoInputData
            .builder()
            .data(Date.from(LocalDateTime.of(2020, 7, 10, 0, 0).toInstant(ZoneOffset.UTC)))
            .incorporacao(1L)
            .tipo(1L)
            .numero("454455")
            .valor(BigDecimal.valueOf(50))
            .uriAnexo("repo1:patrimoniomobiliario/teste.pdf")
            .build();

        mockMvc.perform(
            post("/patrimonios/incorporacao/1/documentos")
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful());
    }

    private void createMockRequisicaoSetup() {

        wireMockRule.stubFor(WireMock.post(urlEqualTo("/setup/hal/public/arquivos/definitivo?uri=repo1:patrimoniomobiliario/teste.pdf"))
            .willReturn(aResponse()
                .withStatus(200)));
    }
}
