package br.com.azi.patrimoniomobiliario.integrationtest.incorporacao.empenho;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.edicao.EditarEmpenhoInputData;
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
import java.math.BigDecimal;
import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Sql("/datasets/incorporacao/empenho/editar-empenho.sql")
@Transactional
public class EditarEmpenhoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveEditarEmpenho() throws Exception {
        Date data = new Date();

        EditarEmpenhoInputData inputData = EditarEmpenhoInputData
            .builder()
            .id(3L)
            .numeroEmpenho("98748946")
            .valorEmpenho(new BigDecimal("5000"))
            .dataEmpenho(data)
            .incorporacaoId(1L)
            .build();

        mockMvc.perform(
            put("/patrimonios/incorporacao/empenhos/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.toJson(inputData))
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id", equalTo(3)))
            .andExpect(jsonPath("$.numeroEmpenho", equalTo("98748946")))
            .andExpect(jsonPath("$.valorEmpenho", equalTo(5000)))
            .andExpect(jsonPath("$.incorporacaoId", equalTo(2)));
    }
}
