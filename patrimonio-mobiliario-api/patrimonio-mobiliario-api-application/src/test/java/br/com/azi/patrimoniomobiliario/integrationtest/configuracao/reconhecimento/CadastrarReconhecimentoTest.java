package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.reconhecimento;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.cadastro.CadastrarReconhecimentoInputData;
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

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CadastrarReconhecimentoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Rollback
    @Transactional
    public void deveSalvarReconhecimento() throws Exception {
        CadastrarReconhecimentoInputData inputData = CadastrarReconhecimentoInputData
            .builder()
            .nome("COMPRAS")
            .situacao("ATIVO")
            .execucaoOrcamentaria(true)
            .empenho(true)
            .notaFiscal(false)
            .build();

        mockMvc.perform(
            post("/reconhecimentos")
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.nome", equalTo("COMPRAS")))
            .andExpect(jsonPath("$.situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.execucaoOrcamentaria", equalTo(true)))
            .andExpect(jsonPath("$.empenho", equalTo(true)))
            .andExpect(jsonPath("$.notaFiscal", equalTo(false)));
    }
}
