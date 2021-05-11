package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.reserva.global.intervalo;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarintervalodisponivel.BuscarIntervaloDisponivelInputData;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.JsonHelper;
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

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Transactional
public class BuscarIntervaloDisponivelTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql({"/datasets/configuracao/reserva/global/intervalo/buscar-intervalo-disponivel.sql"})
    public void deveBuscarProximoIntervaloReserva() throws Exception {
        BuscarIntervaloDisponivelInputData inputData = BuscarIntervaloDisponivelInputData
            .builder()
            .reservaId(1L)
            .orgaoId(1L)
            .quantidadeReservada(50L)
            .items(Collections.emptyList())
            .build();

        mockMvc.perform(post("/configuracao/reservas/1/intervalos/proximoDisponivel")
            .content(JsonHelper.toJson(inputData))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.numeroInicio", Matchers.equalTo(111)))
            .andExpect(jsonPath("$.numeroFim", Matchers.equalTo(160)));
    }

}
