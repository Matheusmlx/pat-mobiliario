package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.reserva.global.intervalo;

import br.com.azi.patrimoniomobiliario.integrationtest.helper.AuthenticationHelper;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Transactional
public class RemoverReservaIntervaloTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql({"/datasets/usuario.sql", "/datasets/configuracao/reserva/global/intervalo/remover-reserva-intervalo.sql"})
    public void deveRemoverReservaIntervalo() throws Exception {
        mockMvc.perform(delete("/configuracao/reservas/1/intervalos/1")
                .headers(AuthenticationHelper.mockHeaders("usuario", "1", "ESTRUTURA_ORGANIZACIONAL"))
                .header("az-roles", "Mobiliario.Normal")
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

}
