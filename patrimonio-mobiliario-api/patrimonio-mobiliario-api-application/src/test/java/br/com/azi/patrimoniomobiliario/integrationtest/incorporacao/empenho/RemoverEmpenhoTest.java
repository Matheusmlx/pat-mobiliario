package br.com.azi.patrimoniomobiliario.integrationtest.incorporacao.empenho;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.remocao.RemoverEmpenhoInputData;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Sql("/datasets/incorporacao/empenho/remover-empenho.sql")
@Transactional
public class RemoverEmpenhoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveRemoverEmpenho() throws Exception {
        RemoverEmpenhoInputData inputData = RemoverEmpenhoInputData
            .builder()
            .id(3L)
            .build();

        mockMvc.perform(
            delete("/patrimonios/incorporacao/empenhos/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.toJson(inputData))
        )
            .andExpect(status().is2xxSuccessful());
    }
}
