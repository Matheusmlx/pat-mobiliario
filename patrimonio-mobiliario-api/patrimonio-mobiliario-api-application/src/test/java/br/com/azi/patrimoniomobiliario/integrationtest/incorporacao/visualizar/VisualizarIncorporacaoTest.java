package br.com.azi.patrimoniomobiliario.integrationtest.incorporacao.visualizar;

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
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Sql({"/datasets/patrimonio/incorporacao/visualizar-incorporacao.sql"})
@Transactional
public class VisualizarIncorporacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarIncorporacaoParaVisualizacao() throws Exception{

        mockMvc.perform(
            get("/patrimonios/incorporacao/1/visualizar")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
            .andExpect(jsonPath("$.codigo", Matchers.equalTo("1531315")))
            .andExpect(jsonPath("$.situacao", Matchers.equalTo("FINALIZADO")))
            .andExpect(jsonPath("$.comodante", Matchers.equalTo("Fábio")));
    }
}
