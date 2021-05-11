package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.reserva.global.intervalo;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.CadastrarReservaIntervaloInputData;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.database.DatabaseLocker;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.PatrimonioEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ReservaEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ReservaIntervaloEntity;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.AuthenticationHelper;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.JsonHelper;
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
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Transactional
public class CadastrarReservaIntervaloTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DatabaseLocker databaseLocker;

    @Test
    @Sql({"/datasets/usuario.sql", "/datasets/configuracao/reserva/global/intervalo/cadastrar-reserva-intervalo.sql"})
    public void deveCadastrarReservaIntervalo() throws Exception {
        CadastrarReservaIntervaloInputData inputData = CadastrarReservaIntervaloInputData
                .builder()
                .reservaId(1L)
                .intervalos(List.of(
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(100L)
                                .preenchimento("MANUAL")
                                .numeroInicio(200L)
                                .numeroFim(299L)
                                .build(),
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(2L)
                                .quantidadeReservada(100L)
                                .preenchimento("AUTOMATICO")
                                .numeroInicio(401L)
                                .numeroFim(500L)
                                .build()))
                .build();

        mockMvc.perform(post("/configuracao/reservas/1/intervalos")
                .headers(AuthenticationHelper.mockHeaders("usuario", "1", "ESTRUTURA_ORGANIZACIONAL"))
                .header("az-roles", "Mobiliario.Normal")
                .cookie(AuthenticationHelper.getCookies())
                .content(JsonHelper.toJson(inputData))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(databaseLocker).lockTableByClass(ReservaIntervaloEntity.class);
    }

}
