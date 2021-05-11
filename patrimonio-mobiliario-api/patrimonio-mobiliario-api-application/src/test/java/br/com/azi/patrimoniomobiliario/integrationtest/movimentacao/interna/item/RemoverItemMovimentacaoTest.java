package br.com.azi.patrimoniomobiliario.integrationtest.movimentacao.interna.item;

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
@Sql({"/datasets/movimentacao/interna/item/remover-item-movimentacao.sql"})
@Rollback
public class RemoverItemMovimentacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveRemoverItemMovimentacao() throws Exception {
        mockMvc.perform(delete("/movimentacoes/internas/2/itens/5"))
            .andExpect(status().is2xxSuccessful());
    }

}
