package br.com.azi.patrimoniomobiliario.integrationtest.comodante;

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
@Transactional
@Sql({"/datasets/comodante/buscar-comodantes.sql"})
public class BuscarComodantesTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarComodantes() throws Exception {
        mockMvc.perform(get("/comodantes?page=1&size=100"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.totalElements", equalTo(7)))
            .andExpect(jsonPath("$.items[0].id", equalTo(1)))
            .andExpect(jsonPath("$.items[0].nome", equalTo("Amanda")))
            .andExpect(jsonPath("$.items[1].id", equalTo(7)))
            .andExpect(jsonPath("$.items[1].nome", equalTo("Carlos")))
            .andExpect(jsonPath("$.items[2].id", equalTo(4)))
            .andExpect(jsonPath("$.items[2].nome", equalTo("Isabella")))
            .andExpect(jsonPath("$.items[3].id", equalTo(6)))
            .andExpect(jsonPath("$.items[3].nome", equalTo("Laura")))
            .andExpect(jsonPath("$.items[4].id", equalTo(2)))
            .andExpect(jsonPath("$.items[4].nome", equalTo("Luíz")))
            .andExpect(jsonPath("$.items[5].id", equalTo(5)))
            .andExpect(jsonPath("$.items[5].nome", equalTo("Sophia")))
            .andExpect(jsonPath("$.items[6].id", equalTo(3)))
            .andExpect(jsonPath("$.items[6].nome", equalTo("Vanessa")));
    }

    @Test
    public void deveBuscarComodantesComConteudo() throws Exception {
        mockMvc.perform(get("/comodantes?page=1&size=100&conteudo=la"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.totalElements", equalTo(2)))
            .andExpect(jsonPath("$.items[0].id", equalTo(4)))
            .andExpect(jsonPath("$.items[0].nome", equalTo("Isabella")))
            .andExpect(jsonPath("$.items[1].id", equalTo(6)))
            .andExpect(jsonPath("$.items[1].nome", equalTo("Laura")));
    }

}
