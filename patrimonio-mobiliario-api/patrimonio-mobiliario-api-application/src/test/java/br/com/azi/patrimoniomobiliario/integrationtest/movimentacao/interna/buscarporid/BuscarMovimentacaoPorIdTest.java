package br.com.azi.patrimoniomobiliario.integrationtest.movimentacao.interna.buscarporid;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarporid.BuscarMovimentacaoPorIdInputData;
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql({"/datasets/movimentacao/interna/buscarporid/buscar-movimentacao.sql"})
@Transactional
public class BuscarMovimentacaoPorIdTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarMovimentacao() throws Exception {
        final BuscarMovimentacaoPorIdInputData inputData = BuscarMovimentacaoPorIdInputData
            .builder()
            .id(20L)
            .build();

        mockMvc.perform(
            get("/movimentacoes/internas/movimentacao/20")
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id", equalTo(20)))
            .andExpect(jsonPath("$.situacao", equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.orgao", equalTo(1)))
            .andExpect(jsonPath("$.setorOrigem", equalTo(1)))
            .andExpect(jsonPath("$.setorDestino", equalTo(2)))
            .andExpect(jsonPath("$.motivoObservacao", equalTo("motivo")))
            .andExpect(jsonPath("$.numeroNotaLancamentoContabil", equalTo("2015NL98576")))
            .andExpect(jsonPath("$.dataNotaLancamentoContabil", is(notNullValue())))
            .andExpect(jsonPath("$.numeroProcesso", equalTo("12345")));
    }
}
