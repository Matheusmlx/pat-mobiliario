package br.com.azi.patrimoniomobiliario.integrationtest.movimentacao.interna.remocao.vazia;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql({"/datasets/movimentacao/interna/remover-movimentacao-interna.sql"})
@Rollback
public class RemoverMovimentacaoInternaVaziaTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveRemoverMovimentacaoInternaVazia() throws Exception {
        mockMvc.perform(
            delete("/movimentacoes/internas/14/vazia"))
            .andExpect(status().is2xxSuccessful());
    }

}
