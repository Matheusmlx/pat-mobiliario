package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.reconhecimento;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.edicao.EditarReconhecimentoInputData;
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

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Sql({"/datasets/configuracao/reconhecimento/editar-reconhecimento.sql"})
@Transactional
public class EditarReconhecimentoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveEditarReconhecimento() throws Exception {

        EditarReconhecimentoInputData inputData = EditarReconhecimentoInputData
            .builder()
            .id(1L)
            .nome("TESTE_EDICAO")
            .situacao("ATIVO")
            .execucaoOrcamentaria(true)
            .notaFiscal(true)
            .empenho(false)
            .build();

        mockMvc.perform(
            put("/reconhecimentos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonHelper.toJson(inputData))
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.nome", equalTo("TESTE_EDICAO")))
            .andExpect(jsonPath("$.situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.execucaoOrcamentaria", equalTo(true)))
            .andExpect(jsonPath("$.notaFiscal", equalTo(true)))
            .andExpect(jsonPath("$.empenho", equalTo(false)));
    }
}
