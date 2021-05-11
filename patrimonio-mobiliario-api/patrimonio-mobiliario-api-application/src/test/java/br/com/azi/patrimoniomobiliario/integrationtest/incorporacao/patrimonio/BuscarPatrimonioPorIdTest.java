package br.com.azi.patrimoniomobiliario.integrationtest.incorporacao.patrimonio;

import br.com.azi.patrimoniomobiliario.integrationtest.helper.AuthenticationHelper;
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

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Sql("/datasets/incorporacao/patrimonio/editar-patrimonio.sql")
@Transactional
public class BuscarPatrimonioPorIdTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveRetornarItemIncorporacao() throws Exception {
        mockMvc.perform(
            get("/patrimonio/1/ficha")
                .headers(AuthenticationHelper.getHeaders())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id", equalTo(1)))
            .andExpect(jsonPath("$.situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.comodante", equalTo("Luiz")))
            .andExpect(jsonPath("$.valorResidual", equalTo(12500.0)))
            .andExpect(jsonPath("$.dadosDepreciacao.valorDepreciacaoMensal", equalTo(35200.0)));
    }

}
