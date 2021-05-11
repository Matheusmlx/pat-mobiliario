package br.com.azi.patrimoniomobiliario.integrationtest.incorporacao.dadosgerais;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.edicao.EditarIncorporacaoInputData;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.JsonHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql({"/datasets/patrimonio/incorporacao/editar-incorporacao.sql"})
@Transactional
public class EditarIncorporacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveEditarIncorporacao() throws Exception {
        EditarIncorporacaoInputData inputData = EditarIncorporacaoInputData
            .builder()
            .id(2L)
            .codigo("15f1")
            .dataRecebimento(Date.from(Instant.now().atZone(ZoneId.systemDefault()).withMonth(8).withDayOfMonth(7).toInstant()))
            .numProcesso("0898")
            .fornecedor(1L)
            .nota("8.6")
            .valorNota(BigDecimal.valueOf(8.6))
            .dataNota(Date.from(Instant.now().atZone(ZoneId.systemDefault()).withMonth(8).withDayOfMonth(3).toInstant()))
            .orgao(3L)
            .convenio(1L)
            .usuario(5L)
            .reconhecimento(1L)
            .empenho(7L)
            .naturezaDespesa(2L)
            .origemComodato(true)
            .comodante(1L)
            .build();

        mockMvc.perform(
            put("/patrimonios/incorporacao/2")
            .content(JsonHelper.toJson(inputData))
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id", equalTo(2)))
            .andExpect(jsonPath("$.fornecedor.id", equalTo(1)))
            .andExpect(jsonPath("$.situacao", equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.origemComodato", equalTo(true)))
            .andExpect(jsonPath("$.comodante.id", equalTo(1)))
            .andExpect(jsonPath("$.comodante.nome", equalTo("João")));
    }
}
