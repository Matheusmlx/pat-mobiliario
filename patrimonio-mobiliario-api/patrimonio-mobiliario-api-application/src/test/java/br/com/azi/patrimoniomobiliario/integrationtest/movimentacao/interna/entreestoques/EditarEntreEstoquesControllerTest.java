package br.com.azi.patrimoniomobiliario.integrationtest.movimentacao.interna.entreestoques;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.edicao.EditarMovimentacaoEntreEstoquesInputData;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.JsonHelper;
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
@Sql({"/datasets/movimentacao/interna/edicao/editar-entre-estoques.sql"})
@Transactional
@Rollback
public class EditarEntreEstoquesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveEditarUmaEntreEstoquesExistente() throws Exception {
        final EditarMovimentacaoEntreEstoquesInputData inputData = EditarMovimentacaoEntreEstoquesInputData
            .builder()
            .orgao(8772L)
            .setorOrigem(1164L)
            .setorDestino(6056L)
            .motivoObservacao("Teste")
            .numeroNotaLancamentoContabil("2020NL123456")
            .dataNotaLancamentoContabil(new Date())
            .numeroProcesso("12345")
            .build();

        mockMvc.perform(
            put("/movimentacoes/internas/entre-estoques/1")
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.situacao", equalTo("EM_ELABORACAO")))

            .andExpect(jsonPath("$.orgao", equalTo(8772)))
            .andExpect(jsonPath("$.setorOrigem", equalTo(1164)))
            .andExpect(jsonPath("$.setorDestino", equalTo(6056)))

            .andExpect(jsonPath("$.motivoObservacao", equalTo("Teste")))
            .andExpect(jsonPath("$.numeroNotaLancamentoContabil", equalTo("2020NL123456")))
            .andExpect(jsonPath("$.dataNotaLancamentoContabil", is(notNullValue())))
            .andExpect(jsonPath("$.numeroProcesso", equalTo("12345")));
    }
}
