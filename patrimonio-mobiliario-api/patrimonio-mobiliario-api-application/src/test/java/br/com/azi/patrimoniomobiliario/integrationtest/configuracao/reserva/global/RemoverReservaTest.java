package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.reserva.global;

import br.com.azi.patrimoniomobiliario.gateway.dataprovider.database.DatabaseLocker;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.PatrimonioEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ReservaEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Transactional
public class RemoverReservaTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DatabaseLocker databaseLocker;

    @Test
    @Sql({"/datasets/configuracao/reserva/global/remover-reserva.sql"})
    public void deveRemoverAReserva() throws Exception {
        mockMvc.perform(delete("/configuracao/reservas/1"))
            .andExpect(status().is2xxSuccessful());

        verify(databaseLocker).lockTableByClass(ReservaEntity.class);
        verify(databaseLocker).lockTableByClass(PatrimonioEntity.class);
    }
}
