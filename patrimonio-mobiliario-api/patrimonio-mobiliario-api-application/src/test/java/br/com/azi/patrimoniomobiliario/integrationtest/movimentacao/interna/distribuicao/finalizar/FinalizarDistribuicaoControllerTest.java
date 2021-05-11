package br.com.azi.patrimoniomobiliario.integrationtest.movimentacao.interna.distribuicao.finalizar;

import br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.distribuicao.finalizar.FinalizarDistribuicaoAmqpSender;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.AuthenticationHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql({"/datasets/movimentacao/interna/finalizacao/finalizar-distribuicao.sql"})
@Transactional
public class FinalizarDistribuicaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FinalizarDistribuicaoAmqpSender finalizarDistribuicaoAmqpSender;

    @Test
    public void devePrepararDistribuicaoParaProcessamentoAssincrono() throws Exception {
        mockMvc.perform(post("/movimentacoes/internas/distribuicao/1/finalizar")
            .headers(AuthenticationHelper.getHeaders())
            .cookie(AuthenticationHelper.getCookies()))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.situacao", equalTo("EM_PROCESSAMENTO")))
            .andExpect(jsonPath("$.dataFinalizacao", notNullValue()));

        verify(finalizarDistribuicaoAmqpSender, times(1)).sendMessage(1L);
    }

    @Test
    public void deveFinalizarDistribuicao() throws Exception {
        mockMvc.perform(post("/movimentacoes/internas/distribuicao/2/finalizar")
            .headers(AuthenticationHelper.getHeaders())
            .cookie(AuthenticationHelper.getCookies()))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id", equalTo(2)))
            .andExpect(jsonPath("$.situacao", equalTo("FINALIZADO")))
            .andExpect(jsonPath("$.dataFinalizacao", notNullValue()));

        verifyZeroInteractions(finalizarDistribuicaoAmqpSender);
    }
}
