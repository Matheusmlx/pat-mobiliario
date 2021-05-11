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
@Sql({"/datasets/incorporacao/patrimonio/buscar-patrimonios-estornados.sql"})
@Transactional
public class BuscarPatrimoniosEstornadosTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarPatrimoniosEstornados() throws Exception {
        mockMvc.perform(get("/patrimonios/incorporacao/7/patrimonios/estornados?page=1&size=10&sort=numero&direction=ASC"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].numero", equalTo("00000001")))
            .andExpect(jsonPath("$.items[0].descricao", equalTo("Item incorporação descrição 1")))
            .andExpect(jsonPath("$.items[0].valor", equalTo(750.15)))
            .andExpect(jsonPath("$.items[0].motivo", equalTo("motivo 1")))
            .andExpect(jsonPath("$.items[1].numero", equalTo("00000002")))
            .andExpect(jsonPath("$.items[1].descricao", equalTo("Item incorporação descrição 1")))
            .andExpect(jsonPath("$.items[1].valor", equalTo(750.15)))
            .andExpect(jsonPath("$.items[1].motivo", equalTo("motivo 2")))
            .andExpect(jsonPath("$.items[2].numero", equalTo("00000003")))
            .andExpect(jsonPath("$.items[2].descricao", equalTo("Item incorporação descrição 2")))
            .andExpect(jsonPath("$.items[2].valor", equalTo(400.0)))
            .andExpect(jsonPath("$.items[2].motivo", equalTo("motivo 3")))
            .andExpect(jsonPath("$.totalElements", equalTo(3)))
            .andExpect(jsonPath("$.totalPages", equalTo(1)));
    }
}
