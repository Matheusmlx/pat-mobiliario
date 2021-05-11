package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.reserva.global;

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
public class BuscarProximoNumeroReservaTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql({"/datasets/configuracao/reserva/global/buscar-proximo-numero-reserva.sql"})
    public void deveRealizarReservaAutomatica() throws Exception {
        mockMvc.perform(get("/configuracao/reservas/proximoNumero")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.proximoNumero", Matchers.equalTo(127)));
    }

}
