package br.com.azi.patrimoniomobiliario.integrationtest.movimentacao.interna.item;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql("/datasets/movimentacao/interna/item/buscar-quantidade-itens-movimentacao.sql")
@Transactional
public class BuscarQuantidadeItensMovimentacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarAQuantidadeDeItensDaMovimentacao() throws Exception {
        mockMvc.perform(
            get("/movimentacoes/internas/1/itens/quantidade")
                .header("az-user", "admin"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.movimentacaoId", equalTo(1)))
            .andExpect(jsonPath("$.quantidadeItens", equalTo(4)));
    }
}
