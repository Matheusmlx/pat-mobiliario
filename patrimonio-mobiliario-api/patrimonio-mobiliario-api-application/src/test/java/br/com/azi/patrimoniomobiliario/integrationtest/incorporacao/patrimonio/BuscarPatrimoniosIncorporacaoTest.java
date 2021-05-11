package br.com.azi.patrimoniomobiliario.integrationtest.incorporacao.patrimonio;

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

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Sql({"/datasets/incorporacao/patrimonio/buscar-patrimonios-incorporacao.sql"})
@Transactional
public class BuscarPatrimoniosIncorporacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarPatrimoniosDasIncorporacoes() throws Exception {
        mockMvc.perform(get("/patrimonios/incorporacao/25/patrimonios?page=1&direction=ASC&size=10&sort=numero"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.totalElements", equalTo(4)))
            .andExpect(jsonPath("$.totalPages", equalTo(1)))
            .andExpect(jsonPath("$.items[0].numero", equalTo("00000111")))
            .andExpect(jsonPath("$.items[0].descricao", equalTo("item incorporacao 1")))
            .andExpect(jsonPath("$.items[0].valor", equalTo(350.50)))
            .andExpect(jsonPath("$.items[1].numero", equalTo("00000112")))
            .andExpect(jsonPath("$.items[1].descricao", equalTo("item incorporacao 2")))
            .andExpect(jsonPath("$.items[1].valor", equalTo(27000.0)))
            .andExpect(jsonPath("$.items[2].numero", equalTo("00000113")))
            .andExpect(jsonPath("$.items[2].descricao", equalTo("item incorporacao 3")))
            .andExpect(jsonPath("$.items[2].valor", equalTo(45344.35)))
            .andExpect(jsonPath("$.items[3].numero", equalTo("00000114")))
            .andExpect(jsonPath("$.items[3].descricao", equalTo("item incorporacao 4")))
            .andExpect(jsonPath("$.items[3].valor", equalTo(98485.50)));
    }
}
