package br.com.azi.patrimoniomobiliario.integrationtest.incorporacao.empenho;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.cadastro.CadastrarEmpenhoInputData;
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

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Sql("/datasets/incorporacao/empenho/cadastrar-empenho.sql")
@Transactional
public class CadastrarEmpenhoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveSalvarEmpenho() throws Exception {
        Date data = new Date();

        CadastrarEmpenhoInputData inputData = CadastrarEmpenhoInputData
            .builder()
            .numeroEmpenho("8613131")
            .valorEmpenho(new BigDecimal("8905.50"))
            .dataEmpenho(data)
            .incorporacaoId(1L)
            .build();

        mockMvc.perform(
            post("/patrimonios/incorporacao/empenhos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonHelper.toJson(inputData))
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.numeroEmpenho", equalTo("8613131")))
            .andExpect(jsonPath("$.incorporacaoId", equalTo(1)));
    }
}
