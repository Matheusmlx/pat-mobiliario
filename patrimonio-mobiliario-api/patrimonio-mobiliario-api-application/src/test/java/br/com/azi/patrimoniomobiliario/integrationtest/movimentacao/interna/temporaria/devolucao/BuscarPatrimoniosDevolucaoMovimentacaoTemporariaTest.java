package br.com.azi.patrimoniomobiliario.integrationtest.movimentacao.interna.temporaria.devolucao;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao.BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputData;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.JsonHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
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
@Rollback
@Sql({"/datasets/movimentacao/interna/temporaria/buscar-listagem-patrimonios-aguardando-devolucao.sql"})
@Transactional
public class BuscarPatrimoniosDevolucaoMovimentacaoTemporariaTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarPatrimoniosAguardandoDevolucao() throws Exception {
       List<BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputData.Item> itensMovimentacaoEsperados =
           Arrays.asList(
               BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputData.Item.builder()
                   .patrimonioDescricao("descricao")
                   .patrimonioId(1L)
                   .patrimonioNumero("0000001")
                   .build(),
               BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputData.Item.builder()
                   .patrimonioDescricao("descricao")
                   .patrimonioId(2L)
                   .patrimonioNumero("0000002")
                   .build());

       String response = mockMvc.perform(
            get("/movimentacoes/internas/temporaria/1/devolver-patrimonios")
                .param("page", "1")
                .param("size", "10")
                .header("az-user", "admin"))
            .andExpect(status().is2xxSuccessful())
            .andReturn()
            .getResponse()
            .getContentAsString();

        BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputData outputData = JsonHelper.fromJson(response,
            BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputData.class);
        assertEquals(2, outputData.getItens().size());
        assertEquals(itensMovimentacaoEsperados, outputData.getItens());
    }
}
