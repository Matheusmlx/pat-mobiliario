package br.com.azi.patrimoniomobiliario.integrationtest.responsavel;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql({"/datasets/responsavel/buscar-responsaveis.sql"})
@Transactional
public class BuscarResponsaveisTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarResponsaveis() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/responsavel"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items.size()", Matchers.equalTo(9)))
            .andExpect(jsonPath("$.items[0].id", Matchers.equalTo(1)))
            .andExpect(jsonPath("$.items[0].nome", Matchers.equalTo("Alice")))
            .andExpect(jsonPath("$.items[1].id", Matchers.equalTo(7)))
            .andExpect(jsonPath("$.items[1].nome", Matchers.equalTo("Ana")))
            .andExpect(jsonPath("$.items[2].id", Matchers.equalTo(5)))
            .andExpect(jsonPath("$.items[2].nome", Matchers.equalTo("Beatriz")))
            .andExpect(jsonPath("$.items[3].id", Matchers.equalTo(6)))
            .andExpect(jsonPath("$.items[3].nome", Matchers.equalTo("Carla")))
            .andExpect(jsonPath("$.items[4].id", Matchers.equalTo(2)))
            .andExpect(jsonPath("$.items[4].nome", Matchers.equalTo("Carol")))
            .andExpect(jsonPath("$.items[5].id", Matchers.equalTo(8)))
            .andExpect(jsonPath("$.items[5].nome", Matchers.equalTo("Diego")))
            .andExpect(jsonPath("$.items[6].id", Matchers.equalTo(4)))
            .andExpect(jsonPath("$.items[6].nome", Matchers.equalTo("Douglas")))
            .andExpect(jsonPath("$.items[7].id", Matchers.equalTo(3)))
            .andExpect(jsonPath("$.items[7].nome", Matchers.equalTo("João")))
            .andExpect(jsonPath("$.items[8].id", Matchers.equalTo(9)))
            .andExpect(jsonPath("$.items[8].nome", Matchers.equalTo("Júlia")));
    }

}
