package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.concedente;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.edicao.EditarConcedenteInputData;
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
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql({"/datasets/configuracao/concedente/editar-concedentes.sql"})
@Transactional
public class EditarConcedenteTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveEditarConcedente() throws Exception {
        EditarConcedenteInputData inputData = EditarConcedenteInputData
            .builder()
            .id(1L)
            .cpfCnpj("89662232060")
            .nome("Teste Editado")
            .situacao("INATIVO")
            .build();

        mockMvc.perform(
            put("/concedentes/1")
            .content(JsonHelper.toJson(inputData))
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.cpfCnpj", equalTo("89662232060")))
            .andExpect(jsonPath("$.nome", equalTo("Teste Editado")))
            .andExpect(jsonPath("$.tipoPessoa", equalTo("FISICA")))
            .andExpect(jsonPath("$.situacao", equalTo("INATIVO")));
    }
}
