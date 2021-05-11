package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.concedente;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarporid.BuscarConcedentePorIdInputData;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.JsonHelper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Sql({"/datasets/configuracao/concedente/buscar-concedentes.sql"})
@Transactional
public class BuscarConcedentePorIdTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarConcedentePorId() throws Exception{

        BuscarConcedentePorIdInputData inputData = BuscarConcedentePorIdInputData
            .builder()
            .id(1L)
            .build();

        mockMvc.perform(
            get("/concedentes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.toJson(inputData))
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.nome", equalTo("A nome concedente")))
            .andExpect(jsonPath("$.situacao", equalTo("ATIVO")));
    }
}
