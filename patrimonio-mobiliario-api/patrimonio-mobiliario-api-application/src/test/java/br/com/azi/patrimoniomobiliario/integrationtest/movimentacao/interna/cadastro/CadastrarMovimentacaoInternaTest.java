package br.com.azi.patrimoniomobiliario.integrationtest.movimentacao.interna.cadastro;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.cadastro.CadastrarMovimentacaoInternaInputData;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.JsonHelper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CadastrarMovimentacaoInternaTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveCadastrarMovimentacaoInterna() throws Exception {
        CadastrarMovimentacaoInternaInputData inputData = CadastrarMovimentacaoInternaInputData
            .builder()
            .tipo("DISTRIBUICAO")
            .build();

        mockMvc.perform(
            post("/movimentacoes/internas")
                .contentType(MediaType.APPLICATION_JSON)
                .header("az-user", "admin")
            .content(JsonHelper.toJson(inputData)))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.codigo", Matchers.equalTo("00001")))
            .andExpect(jsonPath("$.tipo", Matchers.equalTo("DISTRIBUICAO")))
            .andExpect(jsonPath("$.situacao", Matchers.equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.usuarioCriacao", Matchers.equalTo("admin")));
    }

}
