package br.com.azi.patrimoniomobiliario.integrationtest.movimentacao.interna.visualizacao.definitva;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.editar.EditarVisualizacaoInputData;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.JsonHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql({"/datasets/movimentacao/interna/visualizacao/editar-visualizacao.sql"})
@Transactional
public class EditarVisualizacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveEditarAVisualizacaoDaMovimentacao() throws Exception {
        final EditarVisualizacaoInputData inputData = EditarVisualizacaoInputData
            .builder()
            .id(1L)
            .numeroNotaLancamentoContabil("2020NL123456")
            .dataNotaLancamentoContabil(new Date())
            .build();

        mockMvc.perform(
            put("/movimentacoes/internas/visualizacao/1")
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.situacao", equalTo("FINALIZADO")))

            .andExpect(jsonPath("$.orgao", equalTo("DPGE - Defensoria Pública Geral do Estado")))
            .andExpect(jsonPath("$.setorOrigem", equalTo("ARI - Assessoria de Relações Institucionais")))
            .andExpect(jsonPath("$.setorDestino", equalTo("UA - Unidade de Almoxarifado")))

            .andExpect(jsonPath("$.motivoObservacao", equalTo("motivo teste")))
            .andExpect(jsonPath("$.numeroNotaLancamentoContabil", equalTo("2020NL123456")))
            .andExpect(jsonPath("$.dataNotaLancamentoContabil", is(notNullValue())))
            .andExpect(jsonPath("$.numeroProcesso", equalTo("12345")));
    }
}
