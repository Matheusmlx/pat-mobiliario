package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.reserva.pororgao;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximonumero.BuscarProximoNumeroReservaPorOrgaoInputData;
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
public class BuscarProximoNumeroReservaPorOrgaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql({"/datasets/configuracao/reserva/pororgao/buscar-proximo-numero-reserva-por-orgao.sql"})
    public void deveBuscarProximoNumero() throws Exception {

        BuscarProximoNumeroReservaPorOrgaoInputData inputData = new BuscarProximoNumeroReservaPorOrgaoInputData();
        inputData.setOrgaoId(1L);
        mockMvc.perform(post("/configuracao/reservas/proximoNumero")
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.proximoNumero", Matchers.equalTo(151)));
    }

    @Test
    @Sql({"/datasets/configuracao/reserva/pororgao/buscar-proximo-numero-reserva-por-orgao-com-patrimonio.sql"})
    public void deveBuscarProximoNumeroComPatrimonioCadastrado() throws Exception {

        BuscarProximoNumeroReservaPorOrgaoInputData inputData = new BuscarProximoNumeroReservaPorOrgaoInputData();
        inputData.setOrgaoId(1L);
        mockMvc.perform(post("/configuracao/reservas/proximoNumero")
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.proximoNumero", Matchers.equalTo(152)));
    }

    @Test
    @Sql({"/datasets/configuracao/reserva/pororgao/buscar-proximo-numero-reserva-por-orgao.sql"})
    public void deveBuscarProximoNumeroParaOrgaoSemReserva() throws Exception {

        BuscarProximoNumeroReservaPorOrgaoInputData inputData = new BuscarProximoNumeroReservaPorOrgaoInputData();
        inputData.setOrgaoId(3L);
        mockMvc.perform(post("/configuracao/reservas/proximoNumero")
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.proximoNumero", Matchers.equalTo(1)));
    }

}
