package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.reserva.global;

import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.EditarReservaPatrimonialInputData;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.database.DatabaseLocker;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.PatrimonioEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ReservaEntity;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.AuthenticationHelper;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.JsonHelper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Transactional
public class EditarReservaTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DatabaseLocker databaseLocker;

    @Test
    @Sql({"/datasets/usuario.sql", "/datasets/configuracao/reserva/global/editar-reserva.sql"})
    public void deveAtualizarReservaAutomatica() throws Exception {
        EditarReservaPatrimonialInputData inputData = EditarReservaPatrimonialInputData
            .builder()
            .preenchimento(Reserva.Preenchimento.AUTOMATICO.name())
            .quantidadeReservada(127L)
            .build();

        mockMvc.perform(put("/configuracao/reservas/1")
                .content(JsonHelper.toJson(inputData))
                .headers(AuthenticationHelper.mockHeaders("usuario", "1", "ESTRUTURA_ORGANIZACIONAL"))
                .header("az-roles", "Mobiliario.Normal")
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.codigo", Matchers.equalTo("00001")))
                .andExpect(jsonPath("$.situacao", Matchers.equalTo("EM_ELABORACAO")))
                .andExpect(jsonPath("$.preenchimento", Matchers.equalTo("AUTOMATICO")))
                .andExpect(jsonPath("$.quantidadeReservada", Matchers.equalTo(127)))
                .andExpect(jsonPath("$.quantidadeRestante", Matchers.equalTo(127)))
                .andExpect(jsonPath("$.numeroInicio", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.numeroFim", Matchers.equalTo(127)));

        verify(databaseLocker).lockTableByClass(ReservaEntity.class);
        verify(databaseLocker).lockTableByClass(PatrimonioEntity.class);
    }

}
