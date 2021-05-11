package br.com.azi.patrimoniomobiliario.integrationtest.movimentacao.buscarporpatrimonio;

import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;
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

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback
@AutoConfigureMockMvc
@Sql({"/datasets/movimentacao/buscar-movimentacao-patrimonio.sql"})
@Transactional
public class BuscarMovimentacoesPorPatrimonioTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarMovimentacoesPatrimonio() throws Exception {
        String dataFinalizacao = DateValidate.formatarData(LocalDateTime.of(2020, 12, 15, 21, 14, 55));
        String dataCriacao = DateValidate.formatarData(LocalDateTime.of(2020, 11, 15, 21, 14, 55));

        mockMvc.perform(
            get("/patrimonios/10/movimentacoes")
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items.size()", equalTo(2)))
            .andExpect(jsonPath("$.items[0].id", equalTo(16)))
            .andExpect(jsonPath("$.items[0].tipo", equalTo("DISTRIBUICAO")))
            .andExpect(jsonPath("$.items[0].situacao", equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.items[0].motivoObservacao", equalTo("Observacao movimentação")))
            .andExpect(jsonPath("$.items[0].numeroNotaLancamentoContabil", equalTo("4444NL444444")))
            .andExpect(jsonPath("$.items[0].dataNotaLancamentoContabil", equalTo(DateValidate.formatarData(LocalDateTime.of(2021, 1, 5, 0, 0, 0)))))
            .andExpect(jsonPath("$.items[0].orgao", notNullValue()))
            .andExpect(jsonPath("$.items[0].setorOrigem", equalTo("GOV - Governo")))
            .andExpect(jsonPath("$.items[0].setorDestino", equalTo("ADMIN - Administração")))
            .andExpect(jsonPath("$.items[1].id", equalTo(15)))
            .andExpect(jsonPath("$.items[1].tipo", equalTo("TEMPORARIA")))
            .andExpect(jsonPath("$.items[1].dataFinalizacao", equalTo(dataFinalizacao)))
            .andExpect(jsonPath("$.items[1].dataCriacao", equalTo(dataCriacao)))
            .andExpect(jsonPath("$.items[1].numeroNotaLancamentoContabil", equalTo("4444NL444444")))
            .andExpect(jsonPath("$.items[1].valorTotal", equalTo(150.5)))
            .andExpect(jsonPath("$.items[1].situacao", equalTo("FINALIZADO")))
            .andExpect(jsonPath("$.items[1].setorOrigem", equalTo("GOV - Governo")))
            .andExpect(jsonPath("$.items[1].setorDestino", equalTo("ADMIN - Administração")));
    }

}
