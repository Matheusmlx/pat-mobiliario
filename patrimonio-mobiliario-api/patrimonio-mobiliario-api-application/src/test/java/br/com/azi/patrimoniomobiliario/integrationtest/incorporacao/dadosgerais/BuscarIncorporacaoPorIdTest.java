package br.com.azi.patrimoniomobiliario.integrationtest.incorporacao.dadosgerais;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarporid.BuscarIncorporacaoPorIdInputData;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.JsonHelper;
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
@Sql({"/datasets/patrimonio/incorporacao/buscar-incorporacoes.sql"})
@Transactional
public class BuscarIncorporacaoPorIdTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarIncorporacaoPorId() throws Exception{
        BuscarIncorporacaoPorIdInputData inputData = BuscarIncorporacaoPorIdInputData
            .builder()
            .id(1L)
            .build();

        mockMvc.perform(
            get("/patrimonios/incorporacao/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.toJson(inputData))
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
            .andExpect(jsonPath("$.codigo", Matchers.equalTo("1531315")))
            .andExpect(jsonPath("$.situacao", Matchers.equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.origemComodato", Matchers.equalTo(true)))
            .andExpect(jsonPath("$.comodante.id", Matchers.equalTo(1)))
            .andExpect(jsonPath("$.comodante.nome", Matchers.equalTo("João")));
    }
}
