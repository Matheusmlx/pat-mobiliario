package br.com.azi.patrimoniomobiliario.integrationtest.incorporacao.item;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.EditaItemIncorporacaoInputData;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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
@Sql("/datasets/incorporacao/item/editar-item-incorporacao.sql")
public class EditarItemIncorporacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8022);

    @Test
    @Rollback
    @Transactional
    public void deveEditarItemIncorporacao() throws Exception {
        createMockRequisicaoSetup();

        EditaItemIncorporacaoInputData inputData = EditaItemIncorporacaoInputData
            .builder()
            .id(1L)
            .idIncorporacao(1L)
            .marca("marca")
            .modelo("modelo")
            .fabricante("fabricante")
            .valorTotal(BigDecimal.valueOf(1500))
            .numeracaoPatrimonial("AUTOMATICA")
            .naturezaDespesa(2L)
            .estadoConservacao(1L)
            .uriImagem("repo1:patrimoniomobiliario/teste.pdf")
            .anoFabricacaoModelo("2019")
            .combustivel("GASOLINA")
            .categoria("VANS")
            .descricao("descrição")
            .codigo("255555")
            .build();


        mockMvc.perform(
            put("/patrimonios/incorporacao/1/item/1")
                .content(JsonHelper.toJson(inputData))
                .headers(AuthenticationHelper.getHeaders())
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.marca", equalTo("marca")))
            .andExpect(jsonPath("$.modelo", equalTo("modelo")))
            .andExpect(jsonPath("$.fabricante", equalTo("fabricante")))
            .andExpect(jsonPath("$.valorTotal", equalTo(1500)))
            .andExpect(jsonPath("$.numeracaoPatrimonial", equalTo("AUTOMATICA")))
            .andExpect(jsonPath("$.tipoBem", equalTo("VEICULO")))
            .andExpect(jsonPath("$.uriImagem", equalTo("repo1:patrimoniomobiliario/teste.pdf")))
            .andExpect(jsonPath("$.anoFabricacaoModelo", equalTo("2019")))
            .andExpect(jsonPath("$.combustivel", equalTo("GASOLINA")))
            .andExpect(jsonPath("$.categoria", equalTo("VANS")))
            .andExpect(jsonPath("$.descricao", equalTo("descrição")))
            .andExpect(jsonPath("$.codigo", equalTo("255555")))
            .andExpect(jsonPath("$.contaContabil.id", equalTo(41)))
            .andExpect(jsonPath("$.contaContabil.codigo", equalTo("123110103")))
            .andExpect(jsonPath("$.contaContabil.descricao", equalTo("APARELHOS E EQUIPAMENTOS DE COMUNICAÇÃO")))
            .andExpect(jsonPath("$.naturezaDespesa.id", equalTo(2)))
            .andExpect(jsonPath("$.naturezaDespesa.despesa", equalTo("55555555")))
            .andExpect(jsonPath("$.naturezaDespesa.descricao", equalTo("VEICULOS COMBATE")))
            .andDo(print());
    }

    private void createMockRequisicaoSetup() {
        wireMockRule.stubFor(WireMock.post(urlEqualTo("/setup/hal/public/arquivos/definitivo?uri=repo1:patrimoniomobiliario/teste.pdf"))
            .willReturn(aResponse()
                .withStatus(200)));
    }
}
