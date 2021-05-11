package br.com.azi.patrimoniomobiliario.integrationtest.movimentacao.interna.trocartipo;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.trocartipo.TrocarTipoMovimentacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.trocartipo.TrocarTipoMovimentacaoOutputData;
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
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql("/datasets/movimentacao/interna/trocar-tipo-movimentacao.sql")
@Transactional
public class TrocarTipoMovimentacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveGerarUmaNovaMovimentacaoComOTipoSelecionadoQuandoTrocarOTipoDaMovimentacao() throws Exception {
        final TrocarTipoMovimentacaoInputData inputData = TrocarTipoMovimentacaoInputData.builder()
            .tipo("ENTRE_SETORES")
            .build();

        final TrocarTipoMovimentacaoOutputData outputDataEsperado = TrocarTipoMovimentacaoOutputData.builder()
            .id(3L)
            .codigo("00003")
            .tipo("ENTRE_SETORES")
            .situacao("EM_ELABORACAO")
            .usuarioCriacao("admin")
            .build();

        final String response = mockMvc.perform(put("/movimentacoes/internas/1/tipo")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(JsonHelper.toJson(inputData))
            .header("az-user", "admin"))
            .andExpect(status().is2xxSuccessful())
            .andReturn()
            .getResponse()
            .getContentAsString();

        final TrocarTipoMovimentacaoOutputData outputData = JsonHelper.fromJson(response, TrocarTipoMovimentacaoOutputData.class);

        assertEquals(outputDataEsperado, outputData);
    }

    @Test
    public void naoDeveGerarUmaNovaMovimentacaoQuandoOTipoSelecionadoForOMesmoTipoDaMovimentacao() throws Exception {
        final TrocarTipoMovimentacaoInputData inputData = TrocarTipoMovimentacaoInputData.builder()
            .tipo("DISTRIBUICAO")
            .build();

        final TrocarTipoMovimentacaoOutputData outputDataEsperado = TrocarTipoMovimentacaoOutputData.builder()
            .id(1L)
            .codigo("00001")
            .tipo("DISTRIBUICAO")
            .situacao("EM_ELABORACAO")
            .usuarioCriacao("admin")
            .build();

        final String response = mockMvc.perform(put("/movimentacoes/internas/1/tipo")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(JsonHelper.toJson(inputData))
            .header("az-user", "admin"))
            .andExpect(status().is2xxSuccessful())
            .andReturn()
            .getResponse()
            .getContentAsString();

        final TrocarTipoMovimentacaoOutputData outputData = JsonHelper.fromJson(response, TrocarTipoMovimentacaoOutputData.class);

        assertEquals(outputDataEsperado, outputData);
    }
}
