package br.com.azi.patrimoniomobiliario.integrationtest.naturezadespesa;

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
@Sql("/datasets/naturezadespesa/buscar-lista-natureza-despesa.sql")
@Transactional
public class BuscarNaturezasDespesaTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarNaturezasDespesaPorCodigoItemCatalogo () throws Exception{
        mockMvc.perform(
            get("/natureza-despesa?itemCatalogoCodigo=0000019")
                .headers(AuthenticationHelper.getHeaders())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items.length()",equalTo(1)))
            .andExpect(jsonPath("$.items[0].id",equalTo(1)));
    }

}

