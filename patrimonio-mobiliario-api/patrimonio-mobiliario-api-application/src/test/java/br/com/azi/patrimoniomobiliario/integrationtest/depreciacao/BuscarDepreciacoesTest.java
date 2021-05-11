package br.com.azi.patrimoniomobiliario.integrationtest.depreciacao;

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

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Sql({"/datasets/depreciacao/buscar-depreciacoes.sql"})
@Transactional
public class BuscarDepreciacoesTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarDepreciacoes() throws Exception {
        mockMvc.perform(
            get("/patrimonio/1/depreciacoes")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(1)))
            .andExpect(jsonPath("$.items[0].dataInicial", equalTo("2020-01-07T00:00:00")))
            .andExpect(jsonPath("$.items[0].dataFinal", equalTo("2020-01-31T23:59:59")))
            .andExpect(jsonPath("$.items[0].mesReferencia", equalTo("01/2020")))
            .andExpect(jsonPath("$.items[0].valorAnterior", equalTo(1000.0)))
            .andExpect(jsonPath("$.items[0].valorPosterior", equalTo(881.0)))
            .andExpect(jsonPath("$.items[0].valorSubtraido", equalTo(119.0)))
            .andExpect(jsonPath("$.items[0].taxaAplicada", equalTo(11.9)))
            .andExpect(jsonPath("$.items[0].orgaoSigla", equalTo("GOV")))
            .andExpect(jsonPath("$.items[0].setorSigla", equalTo("ADMIN")))
            .andExpect(jsonPath("$.items[1].id", equalTo(2)))
            .andExpect(jsonPath("$.items[1].dataInicial", equalTo("2020-02-01T00:00:00")))
            .andExpect(jsonPath("$.items[1].dataFinal", equalTo("2020-02-29T23:59:59")))
            .andExpect(jsonPath("$.items[1].mesReferencia", equalTo("02/2020")))
            .andExpect(jsonPath("$.items[1].valorAnterior", equalTo(881.0)))
            .andExpect(jsonPath("$.items[1].valorPosterior", equalTo(738.1)))
            .andExpect(jsonPath("$.items[1].valorSubtraido", equalTo(142.9)))
            .andExpect(jsonPath("$.items[1].taxaAplicada", equalTo(14.29)))
            .andExpect(jsonPath("$.items[1].orgaoSigla", equalTo("GOV")))
            .andExpect(jsonPath("$.items[1].setorSigla", equalTo("ADMIN")));
    }
}
