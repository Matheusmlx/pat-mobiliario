package br.com.azi.patrimoniomobiliario.integrationtest.notificacao;

import br.com.azi.patrimoniomobiliario.integrationtest.helper.AuthenticationHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BuscarQuantidadeNotificacoesNaoVisualizadasTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql({"/datasets/usuario.sql", "/datasets/notificacao/buscar-quantidade-notificacoes-nao-visualizadas.sql"})
    public void deveBuscarQuantidadeNotificacoesNaoVisualizadas() throws Exception {
        mockMvc.perform(get("/notificacoes/quantidade-nao-visualizadas")
            .headers(AuthenticationHelper.getHeaders())
            .cookie(AuthenticationHelper.getCookies()))
            .andExpect(jsonPath("$.quantidadeNaoVisualizadas", equalTo(3)));
    }

}
