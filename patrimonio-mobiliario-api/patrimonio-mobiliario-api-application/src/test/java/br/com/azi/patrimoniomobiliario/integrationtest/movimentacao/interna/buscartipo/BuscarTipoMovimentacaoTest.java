package br.com.azi.patrimoniomobiliario.integrationtest.movimentacao.interna.buscartipo;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/datasets/movimentacao/interna/buscar-tipo-movimentacao.sql")
@Rollback
public class BuscarTipoMovimentacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarTipoMovimentacao() throws Exception {
        mockMvc.perform(get("/movimentacoes/internas/7/tipo"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.tipo", Matchers.equalTo("DISTRIBUICAO")));
    }

}
