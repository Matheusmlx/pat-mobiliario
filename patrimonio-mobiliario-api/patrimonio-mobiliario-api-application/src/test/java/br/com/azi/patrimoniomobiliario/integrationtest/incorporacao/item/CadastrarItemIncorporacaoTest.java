package br.com.azi.patrimoniomobiliario.integrationtest.incorporacao.item;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.cadastrar.CadastrarItemIncorporacaoInputData;
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

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Sql("/datasets/incorporacao/item/cadastrar-item-incorporacao.sql")
@Transactional
public class CadastrarItemIncorporacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveSalvarItemIncorporacao () throws Exception{

        CadastrarItemIncorporacaoInputData inputData = CadastrarItemIncorporacaoInputData
            .builder()
            .codigo("0001")
            .descricao("Item da incorporação")
            .incorporacaoId(1L)
            .build();

        mockMvc.perform(
            post("/patrimonios/incorporacao/1/item")
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.codigo", equalTo("0001")));
    }

}
