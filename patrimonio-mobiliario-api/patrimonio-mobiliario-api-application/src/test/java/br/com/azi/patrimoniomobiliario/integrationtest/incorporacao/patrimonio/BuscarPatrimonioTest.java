package br.com.azi.patrimoniomobiliario.integrationtest.incorporacao.patrimonio;

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

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Sql("/datasets/incorporacao/patrimonio/buscar-listagem-patrimonios.sql")
@Transactional
public class BuscarPatrimonioTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarListagemPatrimoniosPorItemIncorporacao() throws Exception {
        mockMvc.perform(
            get("/patrimonios/incorporacao/items/2/patrimonios?page=1&size=2&sort=id&direction=ASC")
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.items[0].id", equalTo(1)))
        .andExpect(jsonPath("$.items[0].numero", equalTo("2398472894")))
        .andExpect(jsonPath("$.totalElements", equalTo(5)));
    }
}
