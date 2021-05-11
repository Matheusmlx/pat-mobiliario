package br.com.azi.patrimoniomobiliario.integrationtest.movimentacao.interna.documento;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.buscar.BuscarDocumentosMovimentacaoOutputData;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.JsonHelper;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql("/datasets/movimentacao/interna/documento/buscar-documentos-movimentacao.sql")
@Transactional
public class BuscarDocumentosMovimentacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveRetornarOsDocumentosDaMovimentacaoQuandoHouverDocumentosVinculados() throws Exception {
        BuscarDocumentosMovimentacaoOutputData outputDataEsperado = BuscarDocumentosMovimentacaoOutputData.builder()
            .items(Arrays.asList(
                BuscarDocumentosMovimentacaoOutputData.Documento.builder()
                    .id(1L)
                    .numero("454453")
                    .data(DateValidate.formatarData(LocalDateTime.of(2020, 6, 2, 0, 0, 0)))
                    .valor(new BigDecimal("1200.000000"))
                    .uriAnexo("repo1:patrimoniomobiliario/teste.pdf")
                    .tipo(1L)
                    .movimentacao(1L)
                    .build(),
                BuscarDocumentosMovimentacaoOutputData.Documento.builder()
                    .id(2L)
                    .numero("454454")
                    .data(DateValidate.formatarData(LocalDateTime.of(2020, 6, 4, 0, 0, 0)))
                    .valor(new BigDecimal("1300.000000"))
                    .uriAnexo(null)
                    .tipo(1L)
                    .movimentacao(1L)
                    .build()
            ))
            .build();

        final String response = mockMvc.perform(
            get("/movimentacoes/internas/1/documentos")
                .header("az-user", "admin"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items", hasSize(2)))
            .andReturn()
            .getResponse()
            .getContentAsString();

        final BuscarDocumentosMovimentacaoOutputData outputData = JsonHelper.fromJson(response, BuscarDocumentosMovimentacaoOutputData.class);
        assertEquals(outputDataEsperado, outputData);
    }

    @Test
    public void deveRetornarListaVaziaQuandoNaoHouverDocumentosVinculadosComMovimentacao() throws Exception {
        mockMvc.perform(
            get("/movimentacoes/internas/2/documentos")
                .header("az-user", "admin"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items", hasSize(0)));
    }
}
