package br.com.azi.patrimoniomobiliario.integrationtest.movimentacao.interna.item;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.listagem.BuscarItensMovimentacaoOutputData;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.JsonHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql("/datasets/movimentacao/interna/item/buscar-itens-movimentacao.sql")
@Transactional
public class BuscarItensMovimentacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarOsItensDaMovimentacao() throws Exception {
        final List<BuscarItensMovimentacaoOutputData.Item> itensMovimentacaoEsperados = Arrays.asList(
            BuscarItensMovimentacaoOutputData.Item.builder()
                .movimentacaoId(1L)
                .patrimonioId(1L)
                .patrimonioNumero("0000001")
                .patrimonioDescricao("VW GOL 1.0")
                .incorporacaoCodigo("785491")
                .build(),
            BuscarItensMovimentacaoOutputData.Item.builder()
                .movimentacaoId(1L)
                .patrimonioId(2L)
                .patrimonioNumero("0000002")
                .patrimonioDescricao("VW GOL 1.0")
                .incorporacaoCodigo("785491")
                .build(),
            BuscarItensMovimentacaoOutputData.Item.builder()
                .movimentacaoId(1L)
                .patrimonioId(4L)
                .patrimonioNumero("0000004")
                .patrimonioDescricao("VW GOL 1.0")
                .incorporacaoCodigo("785491")
                .build(),
            BuscarItensMovimentacaoOutputData.Item.builder()
                .movimentacaoId(1L)
                .patrimonioId(5L)
                .patrimonioNumero("0000005")
                .patrimonioDescricao("VW GOL 1.0")
                .incorporacaoCodigo("785491")
                .build()
        );

        final String response = mockMvc.perform(
            get("/movimentacoes/internas/1/itens")
                .param("page", "1")
                .param("size", "10")
                .header("az-user", "admin"))
            .andExpect(status().is2xxSuccessful())
            .andReturn()
            .getResponse()
            .getContentAsString();

        final BuscarItensMovimentacaoOutputData outputData = JsonHelper.fromJson(response, BuscarItensMovimentacaoOutputData.class);
        assertEquals(4, outputData.getItems().size());
        assertEquals(itensMovimentacaoEsperados, outputData.getItems());
    }
}
