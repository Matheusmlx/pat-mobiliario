package br.com.azi.patrimoniomobiliario.integrationtest.incorporacao.documento;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RemoverDocumentoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Rollback
    @Sql({"/datasets/incorporacao/documento/documentos.sql"})
    @Transactional
    public void deveRemoverDocumento() throws Exception{
        mockMvc.perform(
            delete("/patrimonios/incorporacao/1/documentos/2")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful());
    }

}
