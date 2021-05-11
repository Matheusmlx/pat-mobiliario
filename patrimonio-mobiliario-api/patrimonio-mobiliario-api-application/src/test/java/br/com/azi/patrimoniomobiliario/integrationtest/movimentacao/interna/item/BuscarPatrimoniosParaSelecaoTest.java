package br.com.azi.patrimoniomobiliario.integrationtest.movimentacao.interna.item;

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

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql({"/datasets/movimentacao/interna/item/buscar-patrimonios-para-selecao.sql"})
@Rollback
public class BuscarPatrimoniosParaSelecaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarPatrimoniosQueNaoEstaoEmOutraMovimentacaoEmElaboracao() throws Exception {
        mockMvc.perform(get("/movimentacoes/internas/8/patrimoniosParaSelecao?page=1&size=10&direction=ASC"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].patrimonioId", equalTo(2)))
            .andExpect(jsonPath("$.items[0].patrimonioNumero", equalTo("0000000016")))
            .andExpect(jsonPath("$.items[0].patrimonioDescricao", equalTo("Veículo caminhão leve com as seguintes características técnicas mínimas")))
            .andExpect(jsonPath("$.items[0].incorporacaoCodigo", equalTo("2")))
            .andExpect(jsonPath("$.items[1].patrimonioId", equalTo(3)))
            .andExpect(jsonPath("$.items[1].patrimonioNumero", equalTo("0000000017")))
            .andExpect(jsonPath("$.items[1].patrimonioDescricao", equalTo("Veículo caminhão leve com as seguintes características técnicas mínimas")))
            .andExpect(jsonPath("$.items[1].incorporacaoCodigo", equalTo("2")))
            .andExpect(jsonPath("$.totalElements", equalTo(2)))
            .andExpect(jsonPath("$.totalPages", equalTo(1)));
    }


    @Test
    public void naoDeveTrazerPatrimoniosEmprestadosEmMovimentacaoTemporariaAguardandoDevolucao() throws Exception {
        mockMvc.perform(get("/movimentacoes/internas/10/patrimoniosParaSelecao?page=1&size=10&direction=ASC"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].patrimonioId", equalTo(5)))
            .andExpect(jsonPath("$.items[1].patrimonioId", equalTo(9)))
            .andExpect(jsonPath("$.totalElements", equalTo(2)))
            .andExpect(jsonPath("$.totalPages", equalTo(1)));
    }

    @Test
    public void deveTrazerPatrimoniosJaDevolvidosEmMovimentacoesTemporarias() throws Exception {
        mockMvc.perform(get("/movimentacoes/internas/11/patrimoniosParaSelecao?page=1&size=10&direction=ASC"))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].patrimonioId", equalTo(5)))
            .andExpect(jsonPath("$.items[1].patrimonioId", equalTo(9)))
            .andExpect(jsonPath("$.totalElements", equalTo(2)))
            .andExpect(jsonPath("$.totalPages", equalTo(1)));
    }

}
