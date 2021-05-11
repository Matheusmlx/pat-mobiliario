package br.com.azi.patrimoniomobiliario.integrationtest.incorporacao.item.estadosconservacao;

import br.com.azi.patrimoniomobiliario.integrationtest.helper.AuthenticationHelper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Transactional
public class BuscarEstadosConservacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarEstadosConservacao() throws Exception {
        mockMvc.perform(
            get("/patrimonios/incorporacao/itens/estadosconservacao")
                .headers(AuthenticationHelper.getHeaders())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.estadosConservacao[0].nome", equalTo("Ótimo")))
            .andExpect(jsonPath("$.estadosConservacao[0].prioridade", equalTo(1)))
            .andExpect(jsonPath("$.estadosConservacao[1].nome", equalTo("Bom")))
            .andExpect(jsonPath("$.estadosConservacao[1].prioridade", equalTo(2)))
            .andExpect(jsonPath("$.estadosConservacao[2].nome", equalTo("Regular")))
            .andExpect(jsonPath("$.estadosConservacao[2].prioridade", equalTo(3)))
            .andExpect(jsonPath("$.estadosConservacao[3].nome", equalTo("Desuso")))
            .andExpect(jsonPath("$.estadosConservacao[3].prioridade", equalTo(4)))
            .andExpect(jsonPath("$.estadosConservacao[4].nome", equalTo("Precário")))
            .andExpect(jsonPath("$.estadosConservacao[4].prioridade", equalTo(5)))
            .andExpect(jsonPath("$.estadosConservacao[5].nome", equalTo("Obsoleto")))
            .andExpect(jsonPath("$.estadosConservacao[5].prioridade", equalTo(6)))
            .andExpect(jsonPath("$.estadosConservacao[6].nome", equalTo("Inservível")))
            .andExpect(jsonPath("$.estadosConservacao[6].prioridade", equalTo(7)));
    }

}
