package br.com.azi.patrimoniomobiliario.integrationtest.incorporacao.empenho;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Sql("/datasets/incorporacao/empenho/buscar-empenhos.sql")
@Transactional
public class BuscarEmpenhosTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarListagemEmpenhosPorIncorporacao() throws Exception {
        mockMvc.perform(
            get("/patrimonios/incorporacao/2/empenhos")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.totalElements", equalTo(5)))
            .andExpect(jsonPath("$.items[3].id", equalTo(2)))
            .andExpect(jsonPath("$.items[3].dataEmpenho", equalTo(LocalDateTime.of(2018, 7, 12, 5, 25, 20).toString())))
            .andExpect(jsonPath("$.items[3].numeroEmpenho", equalTo("48646841")))
            .andExpect(jsonPath("$.items[3].valorEmpenho", equalTo(9825.75)));
    }

}
