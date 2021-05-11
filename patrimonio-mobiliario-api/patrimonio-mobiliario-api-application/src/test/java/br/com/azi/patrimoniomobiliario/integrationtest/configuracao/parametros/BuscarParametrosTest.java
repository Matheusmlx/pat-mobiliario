package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.parametros;

import br.com.azi.patrimoniomobiliario.integrationtest.helper.AuthenticationHelper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BuscarParametrosTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarOsParametrosDoSistema() throws Exception {
        this.mockMvc
                .perform(
                        get("/parametros-sistema")
                                .headers(AuthenticationHelper.getHeaders())
                                .cookie(AuthenticationHelper.getCookies())
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.urlChatSuporte", Matchers.equalTo(null)))
                .andExpect(jsonPath("$.quantidadeDigitosNumeroPatrimonio", Matchers.equalTo("10")))
                .andExpect(jsonPath("$.reservaPatrimonialGlobal", Matchers.equalTo(true)));
    }

}
