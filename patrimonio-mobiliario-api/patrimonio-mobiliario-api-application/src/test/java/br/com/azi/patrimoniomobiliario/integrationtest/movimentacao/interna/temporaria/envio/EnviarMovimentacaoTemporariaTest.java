package br.com.azi.patrimoniomobiliario.integrationtest.movimentacao.interna.temporaria.envio;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql({"/datasets/movimentacao/interna/temporaria/enviar-movimentacao-temporaria.sql"})
@Transactional
public class EnviarMovimentacaoTemporariaTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveEnviarMovimentacaoTemporaria() throws Exception {
        mockMvc.perform(
            patch("/movimentacoes/internas/temporaria/1/enviar"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.situacao", equalTo("AGUARDANDO_DEVOLUCAO")));
    }

}
