package br.com.azi.patrimoniomobiliario.integrationtest.incorporacao.reabrir;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Sql({"/datasets/patrimonio/incorporacao/reabrir-incorporacao.sql"})
@Transactional
public class ReabrirIncorporacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveReabrirIncorporacao() throws Exception{
        mockMvc.perform(
            patch("/patrimonios/incorporacao/reabrir/20")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful());
    }
}
