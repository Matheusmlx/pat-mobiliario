package br.com.azi.patrimoniomobiliario.integrationtest.movimentacao.interna.entresetores;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Sql({"/datasets/movimentacao/interna/finalizacao/finalizar-movimentacao-entresetores.sql"})
@Transactional
public class FinalizarMovimentacaoEntreSetoresTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveFinalizarMovimentacaoDefinitiva() throws Exception {
        mockMvc.perform(
            patch("/movimentacoes/internas/definitiva/1/finalizar"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.situacao", equalTo("FINALIZADO")));
    }
}
