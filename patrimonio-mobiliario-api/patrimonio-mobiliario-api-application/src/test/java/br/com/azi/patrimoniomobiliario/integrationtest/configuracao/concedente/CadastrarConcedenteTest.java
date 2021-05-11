package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.concedente;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.cadastro.CadastrarConcedenteInputData;
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
public class CadastrarConcedenteTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Rollback
    @Transactional
    public void deveSalvarConcedente() throws Exception{
        CadastrarConcedenteInputData inputData = CadastrarConcedenteInputData
            .builder()
            .cpfCnpj("438.018.270-38")
            .nome("Mário de Freitas Albuquerque")
            .situacao("ATIVO")
            .build();

        mockMvc.perform(
            post("/concedentes")
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.cpfCnpj", equalTo("438.018.270-38")))
            .andExpect(jsonPath("$.nome", equalTo("Mário de Freitas Albuquerque")))
            .andExpect(jsonPath("$.situacao", equalTo("ATIVO")));
    }
}
