package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.reserva.global;

import br.com.azi.patrimoniomobiliario.integrationtest.helper.AuthenticationHelper;
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
public class BuscarReservaPorIdTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql({"/datasets/usuario.sql", "/datasets/configuracao/reserva/global/buscar-reserva-por-id.sql"})
    public void deveRealizarReservaAutomatica() throws Exception {
        mockMvc.perform(get("/configuracao/reservas/1")
                .headers(AuthenticationHelper.mockHeaders("usuario", "1", "ESTRUTURA_ORGANIZACIONAL"))
                .header("az-roles", "Mobiliario.Normal")
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.codigo", Matchers.equalTo("00001")))
                .andExpect(jsonPath("$.situacao", Matchers.equalTo("EM_ELABORACAO")))
                .andExpect(jsonPath("$.preenchimento", Matchers.equalTo("AUTOMATICO")))
                .andExpect(jsonPath("$.quantidadeReservada", Matchers.equalTo(126)))
                .andExpect(jsonPath("$.quantidadeRestante", Matchers.equalTo(126)))
                .andExpect(jsonPath("$.numeroInicio", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.numeroFim", Matchers.equalTo(126)));
    }
}
