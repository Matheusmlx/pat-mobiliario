package br.com.azi.patrimoniomobiliario.integrationtest.incorporacao.patrimonio;

import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.estornar.EstornarPatrimonioInputData;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.JsonHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql({"/datasets/incorporacao/patrimonio/estornar-patrimonios.sql"})
@Transactional
public class EstornarPatrimonioTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveEstornarPatrimonios() throws Exception{
        EstornarPatrimonioInputData inputData = EstornarPatrimonioInputData
            .builder()
            .patrimoniosId(List.of(1L,2L))
            .incorporacaoId(1L)
            .motivo("motivo")
            .usuario("admin")
            .data(new Date())
            .build();

        mockMvc.perform(
            patch("/patrimonios/incorporacao/items/patrimonios/estornar")
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful());
    }

}
