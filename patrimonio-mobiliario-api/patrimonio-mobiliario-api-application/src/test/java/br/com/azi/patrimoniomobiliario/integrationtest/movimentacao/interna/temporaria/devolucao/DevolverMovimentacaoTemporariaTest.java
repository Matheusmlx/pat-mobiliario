package br.com.azi.patrimoniomobiliario.integrationtest.movimentacao.interna.temporaria.devolucao;

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

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Sql({"/datasets/movimentacao/interna/temporaria/devolver-movimentacao-temporaria.sql"})
@Transactional
public class DevolverMovimentacaoTemporariaTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveDevolverTodosItens() throws Exception {

        mockMvc.perform(
            put("/movimentacoes/internas/temporaria/2/devolver-todos")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id", equalTo(2)));;

    }

}
