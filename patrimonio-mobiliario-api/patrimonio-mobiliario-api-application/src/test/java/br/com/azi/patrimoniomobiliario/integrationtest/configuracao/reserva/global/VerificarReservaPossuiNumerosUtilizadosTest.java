package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.reserva.global;

import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.EditarReservaPatrimonialInputData;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.AuthenticationHelper;
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

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Transactional
public class VerificarReservaPossuiNumerosUtilizadosTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql({"/datasets/configuracao/reserva/global/possui-numeros-utilizados.sql"})
    public void deveRetornarFalseQuandoReservaNaoPossuiNumerosUtilizados() throws Exception {
        mockMvc.perform(get("/configuracao/reservas/1/possuiNumerosUtilizados")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.possuiNumerosUtilizados", equalTo(false)));
    }

    @Test
    @Sql({"/datasets/configuracao/reserva/global/possui-numeros-utilizados.sql"})
    public void deveRetornarTrueQuandoReservaPossuiNumerosUtilizados() throws Exception {
        mockMvc.perform(get("/configuracao/reservas/2/possuiNumerosUtilizados")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.possuiNumerosUtilizados", equalTo(true)));
    }
}
