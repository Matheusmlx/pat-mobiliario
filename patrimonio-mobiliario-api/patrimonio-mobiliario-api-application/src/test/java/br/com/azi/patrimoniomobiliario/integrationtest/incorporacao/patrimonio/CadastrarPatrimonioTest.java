package br.com.azi.patrimoniomobiliario.integrationtest.incorporacao.patrimonio;

import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.cadastrar.CadastrarPatrimonioInputData;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.JsonHelper;
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
import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql("/datasets/incorporacao/patrimonio/cadastrar-patrimonio.sql")
public class CadastrarPatrimonioTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Rollback
    @Transactional
    public void deveSalvarTipoDePatrimonio() throws Exception {
        CadastrarPatrimonioInputData inputData = CadastrarPatrimonioInputData
            .builder()
            .itemIncorporacaoId(1L)
            .contaContabilId(1L)
            .quantidade(2L)
            .valorTotal(BigDecimal.valueOf(10))
            .build();

        mockMvc.perform(
            post("/patrimonios/incorporacao/items/patrimonios")
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Rollback
    @Transactional
    public void deveCalcularValorUnitarioCorretamente() throws Exception {
        CadastrarPatrimonioInputData inputData = CadastrarPatrimonioInputData
            .builder()
            .itemIncorporacaoId(1L)
            .contaContabilId(1L)
            .quantidade(3L)
            .valorTotal(BigDecimal.valueOf(10))
            .build();

        mockMvc.perform(
            post("/patrimonios/incorporacao/items/patrimonios")
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Rollback
    @Transactional
    public void deveAtualizarValoresUnitariosDosPatrimonios() throws Exception {
        CadastrarPatrimonioInputData inputData = CadastrarPatrimonioInputData
            .builder()
            .itemIncorporacaoId(2L)
            .contaContabilId(1L)
            .quantidade(2L)
            .valorTotalAnterior(BigDecimal.valueOf(5000))
            .valorTotal(BigDecimal.valueOf(19.75))
            .build();

        mockMvc.perform(
            post("/patrimonios/incorporacao/items/patrimonios")
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful());
    }
}
