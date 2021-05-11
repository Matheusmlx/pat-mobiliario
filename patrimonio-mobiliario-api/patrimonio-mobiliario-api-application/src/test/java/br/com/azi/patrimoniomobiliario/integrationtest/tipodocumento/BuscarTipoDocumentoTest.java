package br.com.azi.patrimoniomobiliario.integrationtest.tipodocumento;

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
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BuscarTipoDocumentoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Rollback
    @Sql({"/datasets/incorporacao/documento/buscar-tipo-documento.sql"})
    @Transactional
    public void deveBuscarTiposDocumento() throws  Exception{
        mockMvc.perform(
            get("/tiposdocumentos?page=1&size=12")
                .contentType(MediaType.APPLICATION_JSON)

        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].descricao", equalTo("Nota Fiscal")));
    }
}
