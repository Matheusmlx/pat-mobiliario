package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.reserva.global;

import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir.InserirReservaPatrimonialInputData;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.database.DatabaseLocker;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.PatrimonioEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ReservaEntity;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Transactional
public class InserirReservaTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DatabaseLocker databaseLocker;

    @Test
    public void deveRealizarReservaAutomatica() throws Exception {
        InserirReservaPatrimonialInputData inputData = InserirReservaPatrimonialInputData
            .builder()
            .preenchimento(Reserva.Preenchimento.AUTOMATICO.name())
            .quantidadeReservada(30L)
            .build();

        mockMvc.perform(post("/configuracao/reservas")
            .content(JsonHelper.toJson(inputData))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.codigo", Matchers.equalTo("00001")))
            .andExpect(jsonPath("$.situacao", Matchers.equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.preenchimento", Matchers.equalTo("AUTOMATICO")))
            .andExpect(jsonPath("$.quantidadeReservada", Matchers.equalTo(30)))
            .andExpect(jsonPath("$.quantidadeRestante", Matchers.equalTo(30)))
            .andExpect(jsonPath("$.numeroInicio", Matchers.equalTo(1)))
            .andExpect(jsonPath("$.numeroFim", Matchers.equalTo(30)));

        verify(databaseLocker).lockTableByClass(ReservaEntity.class);
        verify(databaseLocker).lockTableByClass(PatrimonioEntity.class);
    }

    @Test
    public void deveRealizarReservaManual() throws Exception {
        InserirReservaPatrimonialInputData inputData = InserirReservaPatrimonialInputData
            .builder()
            .preenchimento(Reserva.Preenchimento.MANUAL.name())
            .numeroInicio(5L)
            .numeroFim(20L)
            .build();

        mockMvc.perform(post("/configuracao/reservas")
            .content(JsonHelper.toJson(inputData))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.codigo", Matchers.equalTo("00001")))
            .andExpect(jsonPath("$.situacao", Matchers.equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.preenchimento", Matchers.equalTo("MANUAL")))
            .andExpect(jsonPath("$.quantidadeReservada", Matchers.equalTo(16)))
            .andExpect(jsonPath("$.quantidadeRestante", Matchers.equalTo(16)))
            .andExpect(jsonPath("$.numeroInicio", Matchers.equalTo(5)))
            .andExpect(jsonPath("$.numeroFim", Matchers.equalTo(20)));

        verify(databaseLocker).lockTableByClass(ReservaEntity.class);
        verify(databaseLocker).lockTableByClass(PatrimonioEntity.class);
    }

}
