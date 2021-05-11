package br.com.azi.patrimoniomobiliario.integrationtest.movimentacao.interna.visualizacao.buscarPorId;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Sql({"/datasets/movimentacao/interna/visualizacao/buscar-ficha-movimentacao.sql"})
@Transactional
public class BuscarFichaMovimentacaoPorIdTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarFichaMovimentacaoInterna() throws Exception {

        mockMvc.perform(
            get("/movimentacoes/internas/visualizacao/1")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.situacao", equalTo("FINALIZADO")))
            .andExpect(jsonPath("$.codigo", equalTo("00152")))
            .andExpect(jsonPath("$.usuarioCriacao", equalTo("admin")))
            .andExpect(jsonPath("$.tipo", equalTo("DISTRIBUICAO")))
            .andExpect(jsonPath("$.orgao", equalTo("DPGE - Defensoria Pública Geral do Estado")))
            .andExpect(jsonPath("$.setorOrigem", equalTo("2SUBDEF - Segunda Subdefensoria Pública-Geral")))
            .andExpect(jsonPath("$.setorDestino", equalTo("1SUBDEF - Primeira Subdefensoria Pública-Geral")))
            .andExpect(jsonPath("$.responsavel", equalTo("Fábio")));
    }
}
