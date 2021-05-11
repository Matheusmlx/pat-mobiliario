package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.reserva.pororgao;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximointervalo.BuscarProximoIntervaloDisponivelPorOrgaoInputData;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.JsonHelper;
import org.hamcrest.Matchers;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Rollback
@Transactional
@SpringBootTest(properties = { "az.patrimonio-mobiliario.reserva-patrimonial-global=false" })
public class BuscarProximoInterValoDisponivelPorOrgaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql({"/datasets/configuracao/reserva/pororgao/buscar-proximo-numero-reserva-por-orgao.sql"})
    public void deveBuscarProximoIntervaloDisponivelPorOrgao() throws Exception {

        BuscarProximoIntervaloDisponivelPorOrgaoInputData inputData = new BuscarProximoIntervaloDisponivelPorOrgaoInputData();
        inputData.setOrgaoId(1L);
        inputData.setQuantidadeReservada(10L);
        mockMvc.perform(post("/configuracao/reservas/proximoIntervalo")
            .content(JsonHelper.toJson(inputData))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.numeroInicio", Matchers.equalTo(151)))
            .andExpect(jsonPath("$.numeroFim", Matchers.equalTo(160)));
    }

    @Test
    @Sql({"/datasets/configuracao/reserva/pororgao/buscar-proximo-numero-reserva-por-orgao-com-patrimonio.sql"})
    public void deveBuscarProximoIntervaloComPatrimonioCadastrado() throws Exception {

        BuscarProximoIntervaloDisponivelPorOrgaoInputData inputData = new BuscarProximoIntervaloDisponivelPorOrgaoInputData();
        inputData.setOrgaoId(1L);
        inputData.setQuantidadeReservada(10L);
        mockMvc.perform(post("/configuracao/reservas/proximoIntervalo")
            .content(JsonHelper.toJson(inputData))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.numeroInicio", Matchers.equalTo(152)))
            .andExpect(jsonPath("$.numeroFim", Matchers.equalTo(161)));

    }

    @Test
    @Sql({"/datasets/configuracao/reserva/pororgao/buscar-proximo-numero-reserva-por-orgao.sql"})
    public void deveBuscarProximoIntervaloDisponivelParaOrgaoSemReserva() throws Exception {
        BuscarProximoIntervaloDisponivelPorOrgaoInputData inputData = new BuscarProximoIntervaloDisponivelPorOrgaoInputData();
        inputData.setOrgaoId(3L);
        inputData.setQuantidadeReservada(10L);
        mockMvc.perform(post("/configuracao/reservas/proximoIntervalo")
            .content(JsonHelper.toJson(inputData))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.numeroInicio", Matchers.equalTo(1)))
            .andExpect(jsonPath("$.numeroFim", Matchers.equalTo(10)));
    }
}
