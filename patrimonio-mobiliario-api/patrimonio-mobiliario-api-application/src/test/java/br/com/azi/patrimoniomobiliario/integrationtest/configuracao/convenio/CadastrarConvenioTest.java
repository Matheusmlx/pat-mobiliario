package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.convenio;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.cadastro.CadastrarConvenioInputData;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.JsonHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CadastrarConvenioTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Rollback
    @Transactional
    public void deveSalvarConvenio() throws Exception{
        CadastrarConvenioInputData inputData = CadastrarConvenioInputData
            .builder()
            .concedente(1L)
            .dataVigenciaInicio(Date.from(LocalDateTime.of(2020, 7, 10, 0, 0).toInstant(ZoneOffset.UTC)))
            .dataVigenciaFim(Date.from(LocalDateTime.of(2020, 7, 10, 0, 0).toInstant(ZoneOffset.UTC)))
            .fonteRecurso("Fonte")
            .numero("1234566")
            .nome("Mário de Freitas Albuquerque")
            .situacao("ATIVO")
            .build();

        mockMvc.perform(
            post("/convenios")
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.concedente", equalTo(1)))
            .andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.nome", equalTo("Mário de Freitas Albuquerque")));
    }
}
