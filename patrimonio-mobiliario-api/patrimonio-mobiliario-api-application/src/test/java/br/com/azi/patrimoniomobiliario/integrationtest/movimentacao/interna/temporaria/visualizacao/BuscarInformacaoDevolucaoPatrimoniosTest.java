package br.com.azi.patrimoniomobiliario.integrationtest.movimentacao.interna.temporaria.visualizacao;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql({"/datasets/movimentacao/interna/temporaria/buscar-informacao-devolucao-patrimonios.sql"})
@Transactional
public class BuscarInformacaoDevolucaoPatrimoniosTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarInformacaoDevolucaoPatrimonios() throws Exception {
        mockMvc.perform(get("/movimentacoes/internas/temporaria/1/visualizacao"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.razaoPatrimoniosDevolvidos", Matchers.equalTo("2/4 patrimônios devolvidos")));
    }

}
