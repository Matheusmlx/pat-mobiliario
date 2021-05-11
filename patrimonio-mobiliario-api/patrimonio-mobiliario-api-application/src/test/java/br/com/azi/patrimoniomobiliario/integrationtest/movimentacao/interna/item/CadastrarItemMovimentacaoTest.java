package br.com.azi.patrimoniomobiliario.integrationtest.movimentacao.interna.item;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.cadastro.CadastrarItemMovimentacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.cadastro.CadastrarItemMovimentacaoOutputData;
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

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql("/datasets/movimentacao/interna/item/cadastrar-item-movimentacao.sql")
@Transactional
public class CadastrarItemMovimentacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveCadastrarOsPatrimoniosSelecionadosComoItensDaMovimentacao() throws Exception {
        final CadastrarItemMovimentacaoInputData inputData = CadastrarItemMovimentacaoInputData
            .builder()
            .patrimoniosId(Arrays.asList(1L, 2L))
            .build();

        final String response = mockMvc.perform(
            post("/movimentacoes/internas/1/itens")
                .contentType(MediaType.APPLICATION_JSON)
                .header("az-user", "admin")
                .content(JsonHelper.toJson(inputData)))
            .andExpect(status().is2xxSuccessful())
            .andReturn()
            .getResponse()
            .getContentAsString();

        final CadastrarItemMovimentacaoOutputData outputData = JsonHelper.fromJson(response, CadastrarItemMovimentacaoOutputData.class);
        assertNotNull(outputData);
        assertEquals(outputData.getItens().size(), 2);

        assertEquals(outputData.getItens().get(0).getMovimentacaoId(), Long.valueOf(1));
        assertEquals(outputData.getItens().get(0).getPatrimonioId(), Long.valueOf(1));

        assertEquals(outputData.getItens().get(1).getMovimentacaoId(), Long.valueOf(1));
        assertEquals(outputData.getItens().get(1).getPatrimonioId(), Long.valueOf(2));
    }

    @Test
    public void deveCadastrarTodosPatrimoniosAtivosQuePertencemAoOrgaoESetorComoItensDaMovimentacao() throws Exception {
        final CadastrarItemMovimentacaoInputData inputData = CadastrarItemMovimentacaoInputData
            .builder()
            .todosSelecionados(true)
            .patrimoniosNaoConsiderar(Collections.singletonList(2L))
            .build();

        final String response = mockMvc.perform(
            post("/movimentacoes/internas/3/itens")
                .contentType(MediaType.APPLICATION_JSON)
                .header("az-user", "admin")
                .content(JsonHelper.toJson(inputData)))
            .andExpect(status().is2xxSuccessful())
            .andReturn()
            .getResponse()
            .getContentAsString();

        final CadastrarItemMovimentacaoOutputData outputData = JsonHelper.fromJson(response, CadastrarItemMovimentacaoOutputData.class);
        assertNotNull(outputData);
        assertEquals(outputData.getItens().size(), 2);

        assertEquals(outputData.getItens().get(0).getMovimentacaoId(), Long.valueOf(3));
        assertEquals(outputData.getItens().get(0).getPatrimonioId(), Long.valueOf(1));

        assertEquals(outputData.getItens().get(1).getMovimentacaoId(), Long.valueOf(3));
        assertEquals(outputData.getItens().get(1).getPatrimonioId(), Long.valueOf(4));
    }
}
