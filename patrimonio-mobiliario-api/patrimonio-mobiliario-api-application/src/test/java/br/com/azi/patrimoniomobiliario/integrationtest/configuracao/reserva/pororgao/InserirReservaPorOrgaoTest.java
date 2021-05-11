package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.reserva.pororgao;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.inserir.InserirReservaPorOrgaoInputData;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.database.DatabaseLocker;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.PatrimonioEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ReservaEntity;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.JsonHelper;
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

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Rollback
@Transactional
@SpringBootTest(properties = { "az.patrimonio-mobiliario.reserva-patrimonial-global=false" })
public class InserirReservaPorOrgaoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DatabaseLocker databaseLocker;

    @Test
    public void deveInserirNovaReservaPorOrgaoPreenchimentoAutomatico() throws Exception {
        InserirReservaPorOrgaoInputData inputData = InserirReservaPorOrgaoInputData
            .builder()
            .preenchimento("AUTOMATICO")
            .orgaoId(1L)
            .quantidadeReservada(8L)
            .build();

        mockMvc.perform(post("/configuracao/reservas")
            .content(JsonHelper.toJson(inputData))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.codigo", equalTo("00001")))
            .andExpect(jsonPath("$.situacao", equalTo("FINALIZADO")))
            .andExpect(jsonPath("$.preenchimento", equalTo("AUTOMATICO")))
            .andExpect(jsonPath("$.quantidadeReservada", equalTo(8)))
            .andExpect(jsonPath("$.quantidadeRestante", equalTo(8)))
            .andExpect(jsonPath("$.numeroInicio", equalTo(1)))
            .andExpect(jsonPath("$.numeroFim", equalTo(8)));

        verify(databaseLocker).lockTableByClass(ReservaEntity.class);
        verify(databaseLocker).lockTableByClass(PatrimonioEntity.class);
    }

    @Test
    public void deveInserirNovaReservaPorOrgaoPreenchimentoManual() throws Exception {
        InserirReservaPorOrgaoInputData inputData = InserirReservaPorOrgaoInputData
            .builder()
            .preenchimento("MANUAL")
            .orgaoId(1L)
            .numeroInicio(8L)
            .numeroFim(16L)
            .build();

        mockMvc.perform(post("/configuracao/reservas")
            .content(JsonHelper.toJson(inputData))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.codigo", equalTo("00001")))
            .andExpect(jsonPath("$.situacao", equalTo("FINALIZADO")))
            .andExpect(jsonPath("$.preenchimento", equalTo("MANUAL")))
            .andExpect(jsonPath("$.quantidadeReservada", equalTo(9)))
            .andExpect(jsonPath("$.quantidadeRestante", equalTo(9)))
            .andExpect(jsonPath("$.numeroInicio", equalTo(8)))
            .andExpect(jsonPath("$.numeroFim", equalTo(16)));

        verify(databaseLocker).lockTableByClass(ReservaEntity.class);
        verify(databaseLocker).lockTableByClass(PatrimonioEntity.class);
    }

}
