package br.com.azi.patrimoniomobiliario.integrationtest.notificacao;

import br.com.azi.patrimoniomobiliario.integrationtest.helper.AuthenticationHelper;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BuscarNotificacoesUsuarioTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql({"/datasets/usuario.sql", "/datasets/notificacao/listagem-notificacoes-usuario.sql"})
    public void deveBuscarNotificacoesUsuario() throws Exception {
        LocalDateTime dataCriacaoIncorporacao = LocalDateTime.of(2021, 2, 19, 13, 17, 37);
        LocalDateTime dataCriacaoDistribuicao = LocalDateTime.of(2021, 2, 19, 16, 17, 37);
        mockMvc.perform(get("/notificacoes?page=1&size=10")
            .headers(AuthenticationHelper.getHeaders())
            .cookie(AuthenticationHelper.getCookies()))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(3)))
            .andExpect(jsonPath("$.items[0].origem", equalTo("DISTRIBUICAO")))
            .andExpect(jsonPath("$.items[0].origemId", equalTo(1)))
            .andExpect(jsonPath("$.items[0].assunto", equalTo("Distribuição 986435122")))
            .andExpect(jsonPath("$.items[0].mensagem", equalTo("Finalizada")))
            .andExpect(jsonPath("$.items[0].dataCriacao", equalTo(DateValidate.formatarData(dataCriacaoDistribuicao))))
            .andExpect(jsonPath("$.items[0].visualizada", equalTo(false)))
            .andExpect(jsonPath("$.items[1].id", equalTo(1)))
            .andExpect(jsonPath("$.items[1].origem", equalTo("INCORPORACAO")))
            .andExpect(jsonPath("$.items[1].origemId", equalTo(1)))
            .andExpect(jsonPath("$.items[1].assunto", equalTo("Incorporação 123456789")))
            .andExpect(jsonPath("$.items[1].mensagem", equalTo("Em Elaboração")))
            .andExpect(jsonPath("$.items[1].dataCriacao", equalTo(DateValidate.formatarData(dataCriacaoIncorporacao))))
            .andExpect(jsonPath("$.items[1].visualizada", equalTo(true)))
            .andExpect(jsonPath("$.totalElements", equalTo(2)))
            .andExpect(jsonPath("$.totalPages", equalTo(1)))
            .andExpect(jsonPath("$.quantidadeNaoVisualizadas", equalTo(0)));
    }

}
