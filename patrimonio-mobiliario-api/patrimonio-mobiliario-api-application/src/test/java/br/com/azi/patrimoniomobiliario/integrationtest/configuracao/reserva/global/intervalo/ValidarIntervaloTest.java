package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.reserva.global.intervalo;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.validarintervalo.ValidarIntervaloInputData;
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

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Transactional
@Sql({"/datasets/configuracao/reserva/global/intervalo/validar-intervalo.sql"})
public class ValidarIntervaloTest {

    @Autowired
    private MockMvc mockMvc;

    @Test

    public void deveValidarIntervalo() throws Exception {
        ValidarIntervaloInputData inputData = new ValidarIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setNumeroInicio(100L);
        inputData.setNumeroFim(150L);
        mockMvc.perform(post("/configuracao/reservas/1/intervalos/validar")
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

}
