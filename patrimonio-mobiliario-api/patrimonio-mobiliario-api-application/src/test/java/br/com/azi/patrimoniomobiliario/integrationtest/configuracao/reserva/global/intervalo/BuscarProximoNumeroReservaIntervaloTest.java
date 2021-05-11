package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.reserva.global.intervalo;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarproximonumero.BuscarProximoNumeroReservaIntervaloInputData;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Transactional
@Sql({"/datasets/configuracao/reserva/global/intervalo/buscar-proximo-numero-intervalo.sql"})
public class BuscarProximoNumeroReservaIntervaloTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarProximoNumero() throws Exception {
        BuscarProximoNumeroReservaIntervaloInputData inputData = new BuscarProximoNumeroReservaIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setMaiorNumeroFimIntervalo(0L);
        mockMvc.perform(post("/configuracao/reservas/1/intervalos/proximoNumero")
            .content(JsonHelper.toJson(inputData))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.proximoNumero", Matchers.equalTo(150)));
    }


    @Test
    public void deveRetornarNullSeMaiorNumeroFimDoIntervaloIgualNumeroFimDaReserva() throws Exception {
        BuscarProximoNumeroReservaIntervaloInputData inputData = new BuscarProximoNumeroReservaIntervaloInputData();
        inputData.setReservaId(2L);
        inputData.setMaiorNumeroFimIntervalo(200L);
        mockMvc.perform(post("/configuracao/reservas/2/intervalos/proximoNumero")
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.proximoNumero", Matchers.equalTo(null)));
    }

}
