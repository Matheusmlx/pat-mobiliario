package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.reserva.global.intervalo;

import org.hamcrest.Matchers;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Transactional
public class BuscarReservaIntervalosTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql({"/datasets/configuracao/reserva/global/intervalo/buscar-reserva-intervalos.sql"})
    public void deveBuscarOsIntervaloDaReserva() throws Exception {
        mockMvc.perform(get("/configuracao/reservas/1/intervalos?page=1&size=10")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", Matchers.equalTo(1)))
            .andExpect(jsonPath("$.items[0].codigo", Matchers.equalTo("00001")))
            .andExpect(jsonPath("$.items[0].orgao", Matchers.equalTo("GOV - Governo")))
            .andExpect(jsonPath("$.items[0].situacao", Matchers.equalTo("FINALIZADO")))
            .andExpect(jsonPath("$.items[0].quantidadeReservada", Matchers.equalTo(10)))
            .andExpect(jsonPath("$.items[0].preenchimento", Matchers.equalTo("AUTOMATICO")))
            .andExpect(jsonPath("$.items[0].numeroInicio", Matchers.equalTo(101)))
            .andExpect(jsonPath("$.items[0].numeroFim", Matchers.equalTo(110)))
            .andExpect(jsonPath("$.items[1].id", Matchers.equalTo(2)))
            .andExpect(jsonPath("$.items[1].codigo", Matchers.equalTo("00002")))
            .andExpect(jsonPath("$.items[1].orgao", Matchers.equalTo("GOV - Governo")))
            .andExpect(jsonPath("$.items[1].situacao", Matchers.equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.items[1].quantidadeReservada", Matchers.equalTo(10)))
            .andExpect(jsonPath("$.items[1].preenchimento", Matchers.equalTo("MANUAL")))
            .andExpect(jsonPath("$.items[1].numeroInicio", Matchers.equalTo(111)))
            .andExpect(jsonPath("$.items[1].numeroFim", Matchers.equalTo(120)))
            .andExpect(jsonPath("$.totalElements", Matchers.equalTo(2)));
    }

}
