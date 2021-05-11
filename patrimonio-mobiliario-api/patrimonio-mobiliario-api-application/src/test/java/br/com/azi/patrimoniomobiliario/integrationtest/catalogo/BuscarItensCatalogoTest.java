package br.com.azi.patrimoniomobiliario.integrationtest.catalogo;

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
@Rollback
@Sql({"/datasets/catalogo/buscar-itens.sql"})
@Transactional
public class BuscarItensCatalogoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarItensCatalogoSortCodigoDESC() throws Exception{
        mockMvc.perform(
            get("/catalogo/itens?page=1&size=10&sort=codigo&direction=DESC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.totalElements", equalTo(6)))
            .andExpect(jsonPath("$.totalPages", equalTo(0)))
            .andExpect(jsonPath("$.items[0].id", equalTo(3)))
            .andExpect(jsonPath("$.items[0].codigo", equalTo("6")))
            .andExpect(jsonPath("$.items[1].id", equalTo(3)))
            .andExpect(jsonPath("$.items[1].codigo", equalTo("5")))
            .andExpect(jsonPath("$.items[2].id", equalTo(2)))
            .andExpect(jsonPath("$.items[2].codigo", equalTo("4")))
            .andExpect(jsonPath("$.items[3].id", equalTo(2)))
            .andExpect(jsonPath("$.items[3].codigo", equalTo("3")))
            .andExpect(jsonPath("$.items[4].id", equalTo(1)))
            .andExpect(jsonPath("$.items[4].codigo", equalTo("2")))
            .andExpect(jsonPath("$.items[5].id", equalTo(1)))
            .andExpect(jsonPath("$.items[5].codigo", equalTo("1")));
    }

    @Test
    public void deveBuscarItensCatalogoSortCodigoASC() throws Exception{
        mockMvc.perform(
            get("/catalogo/itens?page=1&size=10&sort=codigo&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(1)))
            .andExpect(jsonPath("$.items[0].codigo", equalTo("1")))
            .andExpect(jsonPath("$.items[1].id", equalTo(1)))
            .andExpect(jsonPath("$.items[1].codigo", equalTo("2")))
            .andExpect(jsonPath("$.items[2].id", equalTo(2)))
            .andExpect(jsonPath("$.items[2].codigo", equalTo("3")))
            .andExpect(jsonPath("$.items[3].id", equalTo(2)))
            .andExpect(jsonPath("$.items[3].codigo", equalTo("4")))
            .andExpect(jsonPath("$.items[4].id", equalTo(3)))
            .andExpect(jsonPath("$.items[4].codigo", equalTo("5")))
            .andExpect(jsonPath("$.items[5].id", equalTo(3)))
            .andExpect(jsonPath("$.items[5].codigo", equalTo("6")));
    }

    @Test
    public void deveBuscarItensCatalogoSortDescricaoDESC() throws Exception{
        mockMvc.perform(
            get("/catalogo/itens?page=1&size=10&sort=descricao&direction=DESC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(3)))
            .andExpect(jsonPath("$.items[0].descricao", equalTo("descricao 3 simplificado")))
            .andExpect(jsonPath("$.items[1].id", equalTo(3)))
            .andExpect(jsonPath("$.items[1].descricao", equalTo("descricao 3 regular")))
            .andExpect(jsonPath("$.items[2].id", equalTo(1)))
            .andExpect(jsonPath("$.items[2].descricao", equalTo("descricao 2 simplificado")))
            .andExpect(jsonPath("$.items[3].id", equalTo(1)))
            .andExpect(jsonPath("$.items[3].descricao", equalTo("descricao 2 regular")))
            .andExpect(jsonPath("$.items[4].id", equalTo(2)))
            .andExpect(jsonPath("$.items[4].descricao", equalTo("descricao 1 simplificado")))
            .andExpect(jsonPath("$.items[5].id", equalTo(2)))
            .andExpect(jsonPath("$.items[5].descricao", equalTo("descricao 1 regular")));
    }

    @Test
    public void deveBuscarItensCatalogoSortDescricaoASC() throws Exception{
        mockMvc.perform(
            get("/catalogo/itens?page=1&size=10&sort=descricao&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(2)))
            .andExpect(jsonPath("$.items[0].descricao", equalTo("descricao 1 regular")))
            .andExpect(jsonPath("$.items[1].id", equalTo(2)))
            .andExpect(jsonPath("$.items[1].descricao", equalTo("descricao 1 simplificado")))
            .andExpect(jsonPath("$.items[2].id", equalTo(1)))
            .andExpect(jsonPath("$.items[2].descricao", equalTo("descricao 2 regular")))
            .andExpect(jsonPath("$.items[3].id", equalTo(1)))
            .andExpect(jsonPath("$.items[3].descricao", equalTo("descricao 2 simplificado")))
            .andExpect(jsonPath("$.items[4].id", equalTo(3)))
            .andExpect(jsonPath("$.items[4].descricao", equalTo("descricao 3 regular")))
            .andExpect(jsonPath("$.items[5].id", equalTo(3)))
            .andExpect(jsonPath("$.items[5].descricao", equalTo("descricao 3 simplificado")));
    }

    @Test
    public void deveBuscarItensCatalogoComAtributos() throws Exception{
        mockMvc.perform(
            get("/catalogo/itens?page=1&size=10&sort=descricao&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(2)))
            .andExpect(jsonPath("$.items[0].codigo", equalTo("3")))
            .andExpect(jsonPath("$.items[0].descricao", equalTo("descricao 1 regular")));
    }

    @Test
    public void deveBuscarItensCatalogoComConteudo() throws Exception{
        mockMvc.perform(
            get("/catalogo/itens?conteudo=descricao 1&page=1&size=10&sort=descricao&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items.length()", equalTo(2)))
            .andExpect(jsonPath("$.items[0].id", equalTo(2)))
            .andExpect(jsonPath("$.items[0].codigo", equalTo("3")))
            .andExpect(jsonPath("$.items[0].descricao", equalTo("descricao 1 regular")))
            .andExpect(jsonPath("$.items[1].id", equalTo(2)))
            .andExpect(jsonPath("$.items[1].codigo", equalTo("4")))
            .andExpect(jsonPath("$.items[1].descricao", equalTo("descricao 1 simplificado")));
    }
}
