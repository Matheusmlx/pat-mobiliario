package br.com.azi.patrimoniomobiliario.integrationtest.incorporacao.patrimonio;

import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.EditarPatrimonioInputData;
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Sql("/datasets/incorporacao/patrimonio/editar-patrimonio.sql")
@Transactional
public class EditarPatrimonioTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveEditarPatrimonio() throws Exception {
        EditarPatrimonioInputData inputData = EditarPatrimonioInputData
            .builder()
            .id(2L)
            .placa("NBS-2019")
            .renavam("05606 386611")
            .chassi("1G1-PC5SB-X-D7301396")
            .build();

        mockMvc.perform(
            put("/patrimonios/incorporacao/items/patrimonios/2")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonHelper.toJson(inputData))
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.placa", equalTo("NBS2019")))
            .andExpect(jsonPath("$.renavam", equalTo("05606386611")))
            .andExpect(jsonPath("$.chassi", equalTo("1G1PC5SBXD7301396")))
            .andExpect(jsonPath("$.licenciamento", equalTo(null)));
    }

    @Test
    public void deveEditarPatrimonioArmamento() throws Exception {
        EditarPatrimonioInputData inputData = EditarPatrimonioInputData
            .builder()
            .id(2L)
            .numeroSerie("951e23a")
            .build();

        mockMvc.perform(
            put("/patrimonios/incorporacao/items/patrimonios/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.toJson(inputData))
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.numeroSerie", equalTo("951e23a")));
    }
}
