package br.com.azi.patrimoniomobiliario.integrationtest.fusohorario;


import br.com.azi.patrimoniomobiliario.integrationtest.helper.AuthenticationHelper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Transactional
public class buscarFusoHorarioTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarFusoHorario() throws Exception{
        this.mockMvc
            .perform(
                get("/fuso-horario")
                    .headers(AuthenticationHelper.getHeaders())
                    .cookie(AuthenticationHelper.getCookies())
            )
            .andExpect(status().is2xxSuccessful());
    }
}
