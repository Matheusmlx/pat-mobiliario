package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.reserva.global.intervalo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Transactional
@Sql({"/datasets/configuracao/reserva/global/intervalo/existe-por-situacao.sql"})
public class ExisteIntervalosPorSituacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveRetornarVerdadeiroQuandoExistirIntervalosComASituacaoInformada() throws Exception {
        mockMvc.perform(get("/configuracao/reservas/1/intervalos/existe/EM_ELABORACAO"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.reservaId", equalTo(1)))
            .andExpect(jsonPath("$.intervaloSituacao", equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.existe", equalTo(true)));
    }

    @Test
    public void deveRetornarFalsoQuandoNaoExistirIntervalosComASituacaoInformada() throws Exception {
        mockMvc.perform(get("/configuracao/reservas/2/intervalos/existe/EM_ELABORACAO"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.reservaId", equalTo(2)))
            .andExpect(jsonPath("$.intervaloSituacao", equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.existe", equalTo(false)));
    }
}
