package br.com.azi.patrimoniomobiliario.integrationtest.incorporacao.finalizar;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.finalizar.finalizar.FinalizarIncorporacaoInputData;
import br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.finalizar.FinalizarIncorporacaoAmqpSender;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql({"/datasets/patrimonio/incorporacao/finalizar-incorporacao.sql"})
@Transactional
public class FinalizarIncorporacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FinalizarIncorporacaoAmqpSender finalizarIncorporacaoAmqpSender;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8022);

    @Test
    public void deveFinalizarIncorporacaoSincronaQuandoNumeroPatrimoniosEstiverDentroDoParametro() throws Exception {
        createMockRequisicaoSetup();

        FinalizarIncorporacaoInputData inputData = FinalizarIncorporacaoInputData.builder().id(1L).build();

        mockMvc.perform(
            patch("/patrimonios/incorporacao/finalizar/1")
                .content(JsonHelper.toJson(inputData))
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.codigo", equalTo("00001")))
            .andExpect(jsonPath("$.situacao", equalTo("FINALIZADO")));

        verifyZeroInteractions(finalizarIncorporacaoAmqpSender);
    }

    @Test
    public void deveFinalizarIncorporacaoAssincronaQuandoNumeroPatrimoniosEstiverAcimaDoParametro() throws Exception {
        createMockRequisicaoSetup();

        FinalizarIncorporacaoInputData inputData = FinalizarIncorporacaoInputData.builder().id(1L).build();

        mockMvc.perform(
            patch("/patrimonios/incorporacao/finalizar/2")
                .content(JsonHelper.toJson(inputData))
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id", equalTo(2)))
            .andExpect(jsonPath("$.codigo", equalTo("00002")))
            .andExpect(jsonPath("$.situacao", equalTo("EM_PROCESSAMENTO")));

        verify(finalizarIncorporacaoAmqpSender, times(1)).sendMessage(2L);
    }

    private void createMockRequisicaoSetup() {
        wireMockRule.stubFor(WireMock.post(urlEqualTo("/setup/hal/public/arquivos/definitivo?uri=repo1:patrimoniomobiliario/teste.pdf"))
            .willReturn(aResponse()
                .withStatus(200)));
    }

}
