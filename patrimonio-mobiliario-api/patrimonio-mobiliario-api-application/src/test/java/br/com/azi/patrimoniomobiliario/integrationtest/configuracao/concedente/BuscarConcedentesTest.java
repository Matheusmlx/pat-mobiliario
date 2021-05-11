package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.concedente;

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
@Sql({"/datasets/configuracao/concedente/buscar-concedentes.sql"})
@Transactional
public class BuscarConcedentesTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarConcedentesSortNomeDESC() throws Exception{
        mockMvc.perform(
            get("/concedentes?page=1&size=10&sort=nome&direction=DESC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(3)))
            .andExpect(jsonPath("$.items[1].id", equalTo(1)))
            .andExpect(jsonPath("$.items[2].id", equalTo(2)))
            .andExpect(jsonPath("$.items[0].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[1].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[2].situacao", equalTo("INATIVO")));
    }

    @Test
    public void deveBuscarConcedentesSortNomeASC() throws Exception{
        mockMvc.perform(
            get("/concedentes?page=1&size=10&sort=nome&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(1)))
            .andExpect(jsonPath("$.items[1].id", equalTo(3)))
            .andExpect(jsonPath("$.items[2].id", equalTo(2)))
            .andExpect(jsonPath("$.items[0].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[1].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[2].situacao", equalTo("INATIVO")));
    }

    @Test
    public void deveBuscarConcedentesSortNomeSituacaoAtivoDesc() throws Exception{
        mockMvc.perform(
            get("/concedentes?situacao=ATIVO&page=1&size=10&sort=nome&direction=DESC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(3)))
            .andExpect(jsonPath("$.items[1].id", equalTo(1)))
            .andExpect(jsonPath("$.items[0].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[1].situacao", equalTo("ATIVO")));
    }

    @Test
    public void deveBuscarConcedentesSortNomeSituacaoAtivoAsc() throws Exception{
        mockMvc.perform(
            get("/concedentes?situacao=ATIVO&page=1&size=10&sort=nome&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(1)))
            .andExpect(jsonPath("$.items[1].id", equalTo(3)))
            .andExpect(jsonPath("$.items[0].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[1].situacao", equalTo("ATIVO")));
    }

    @Test
    public void deveBuscarConcedentesComConteudoNome() throws Exception{
        mockMvc.perform(
            get("/concedentes?conteudo=concedente&page=1&size=10&sort=nome&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(1)))
            .andExpect(jsonPath("$.items[1].id", equalTo(2)))
            .andExpect(jsonPath("$.items[0].nome", equalTo("A nome concedente")))
            .andExpect(jsonPath("$.items[1].nome", equalTo("B nome concedente")))
            .andExpect(jsonPath("$.items[0].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[1].situacao", equalTo("INATIVO")));
    }

    @Test
    public void deveBuscarConcedentesComConteudoSituacao() throws Exception{
        mockMvc.perform(
            get("/concedentes?conteudo=INATIVO&page=1&size=10&sort=nome&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].situacao", equalTo("INATIVO")));
    }

    @Test
    public void deveBuscarConcedentesComSortSituacaoASC() throws Exception{
        mockMvc.perform(
            get("/concedentes?page=1&size=10&sort=situacao&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[1].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[2].situacao", equalTo("INATIVO")));
    }

    @Test
    public void deveBuscarConcedentesComSortSituacaoDESC() throws Exception{
        mockMvc.perform(
            get("/concedentes?page=1&size=10&sort=situacao&direction=DESC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].situacao", equalTo("INATIVO")))
            .andExpect(jsonPath("$.items[1].situacao", equalTo("ATIVO")));
    }

    @Test
    public void deveBuscarConcedentesComSortCpfCnpjASC() throws Exception{
        mockMvc.perform(
            get("/concedentes?page=1&size=10&sort=cpfCnpj&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].cpfCnpj", equalTo("201.222.510-16")))
            .andExpect(jsonPath("$.items[1].cpfCnpj", equalTo("626.527.060-55")))
            .andExpect(jsonPath("$.items[2].cpfCnpj", equalTo("471.849.790-60")))
            .andExpect(jsonPath("$.items[0].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[1].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[2].situacao", equalTo("INATIVO")));
    }

    @Test
    public void deveBuscarConcedentesComSortCpfCnpjDESC() throws Exception{
        mockMvc.perform(
            get("/concedentes?page=1&size=10&sort=cpfCnpj&direction=DESC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].cpfCnpj", equalTo("626.527.060-55")))
            .andExpect(jsonPath("$.items[1].cpfCnpj", equalTo("201.222.510-16")))
            .andExpect(jsonPath("$.items[2].cpfCnpj", equalTo("471.849.790-60")))
            .andExpect(jsonPath("$.items[0].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[1].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[2].situacao", equalTo("INATIVO")));
    }

    @Test
    public void deveBuscarConcedentesSortCadastro() throws Exception{
        mockMvc.perform(
            get("/concedentes?page=1&size=10&sort=situacao&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(1)))
            .andExpect(jsonPath("$.items[1].id", equalTo(3)))
            .andExpect(jsonPath("$.items[2].id", equalTo(2)))
            .andExpect(jsonPath("$.items[0].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[1].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[2].situacao", equalTo("INATIVO")));
    }

    @Test
    public void deveBuscarConcedentesSortEdicao() throws Exception{
        mockMvc.perform(
            get("/concedentes?page=1&size=10&sort=situacao&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(1)))
            .andExpect(jsonPath("$.items[1].id", equalTo(3)))
            .andExpect(jsonPath("$.items[2].id", equalTo(2)))
            .andExpect(jsonPath("$.items[0].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[1].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[2].situacao", equalTo("INATIVO")));
    }
}
