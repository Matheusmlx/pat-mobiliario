package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.convenio;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.edicao.EditarConvenioInputData;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Sql({"/datasets/configuracao/convenio/editar-convenio.sql"})
@Transactional
public class EditarConvenioTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveEditarConvenio() throws Exception {

        EditarConvenioInputData inputData = EditarConvenioInputData
            .builder()
            .id(1L)
            .numero("1234566")
            .nome("TESTE_EDICAO")
            .dataVigenciaInicio(Date.from(LocalDateTime.of(2020, 7, 10, 0, 0).toInstant(ZoneOffset.UTC)))
            .dataVigenciaFim(Date.from(LocalDateTime.of(2020, 7, 10, 0, 0).toInstant(ZoneOffset.UTC)))
            .situacao("ATIVO")
            .concedente(1L)
            .fonteRecurso("Fonte")
            .build();

        mockMvc.perform(
            put("/convenios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.toJson(inputData))
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.nome", equalTo("TESTE_EDICAO")))
            .andExpect(jsonPath("$.concedente", equalTo(1)));
    }
}
