package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.convenio;

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
@Sql({"/datasets/configuracao/convenio/buscar-convenios.sql"})
@Transactional
public class BuscarConveniosTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarConveniosSortNomeDESC() throws Exception{
        mockMvc.perform(
            get("/convenios?page=1&size=10&sort=nome&direction=DESC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(3)))
            .andExpect(jsonPath("$.items[1].id", equalTo(1)))
            .andExpect(jsonPath("$.items[2].id", equalTo(2)))
            .andExpect(jsonPath("$.items[0].concedente.nome", equalTo("C concedente")));
    }

    @Test
    public void deveBuscarConveniosSortNomeASC() throws Exception{
        mockMvc.perform(
            get("/convenios?page=1&size=10&sort=nome&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(1)))
            .andExpect(jsonPath("$.items[1].id", equalTo(3)))
            .andExpect(jsonPath("$.items[2].id", equalTo(2)));
    }

    @Test
    public void deveBuscarConveniosSortNumeroDESC() throws Exception{
        mockMvc.perform(
            get("/convenios?page=1&size=10&sort=numero&direction=DESC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(3)))
            .andExpect(jsonPath("$.items[1].id", equalTo(1)))
            .andExpect(jsonPath("$.items[2].id", equalTo(2)));
    }

    @Test
    public void deveBuscarConveniosSortNumeroASC() throws Exception{
        mockMvc.perform(
            get("/convenios?page=1&size=10&sort=numero&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(1)))
            .andExpect(jsonPath("$.items[1].id", equalTo(3)))
            .andExpect(jsonPath("$.items[2].id", equalTo(2)));
    }

    @Test
    public void deveBuscarConveniosSortConcedenteDESC() throws Exception{
        mockMvc.perform(
            get("/convenios?page=1&size=10&sort=concedente&direction=DESC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(3)))
            .andExpect(jsonPath("$.items[1].id", equalTo(1)))
            .andExpect(jsonPath("$.items[2].id", equalTo(2)));
    }

    @Test
    public void deveBuscarConveniosSortConcedenteASC() throws Exception{
        mockMvc.perform(
            get("/convenios?page=1&size=10&sort=concedente&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(1)))
            .andExpect(jsonPath("$.items[1].id", equalTo(3)))
            .andExpect(jsonPath("$.items[2].id", equalTo(2)));
    }

    @Test
    public void deveBuscarConveniosSortFonteRecursoDESC() throws Exception{
        mockMvc.perform(
            get("/convenios?page=1&size=10&sort=fonteRecurso&direction=DESC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(3)))
            .andExpect(jsonPath("$.items[1].id", equalTo(1)))
            .andExpect(jsonPath("$.items[2].id", equalTo(2)));
    }

    @Test
    public void deveBuscarConveniosSortFonteRecursoASC() throws Exception{
        mockMvc.perform(
            get("/convenios?page=1&size=10&sort=fonteRecurso&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(1)))
            .andExpect(jsonPath("$.items[1].id", equalTo(3)))
            .andExpect(jsonPath("$.items[2].id", equalTo(2)));
    }

    @Test
    public void deveBuscarConveniosComSortSituacaoASC() throws Exception{
        mockMvc.perform(
            get("/convenios?page=1&size=10&sort=situacao&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[1].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[2].situacao", equalTo("INATIVO")));
    }

    @Test
    public void deveBuscarConveniosComSortSituacaoDESC() throws Exception{
        mockMvc.perform(
            get("/convenios?page=1&size=10&sort=situacao&direction=DESC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].situacao", equalTo("INATIVO")))
            .andExpect(jsonPath("$.items[1].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[2].situacao", equalTo("ATIVO")));
    }

    @Test
    public void deveBuscarConveniosComConteudoNumero() throws Exception{
        mockMvc.perform(
            get("/convenios?conteudo=1&page=1&size=10&sort=situacao&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(1)))
            .andExpect(jsonPath("$.items[0].nome", equalTo("convenio alagoas")))
            .andExpect(jsonPath("$.items[0].concedente.nome", equalTo("B nome concedente")))
            .andExpect(jsonPath("$.items[0].concedente.id", equalTo(1)));
    }
    @Test
    public void deveBuscarConveniosComConteudoNome() throws Exception{
        mockMvc.perform(
            get("/convenios?conteudo=convenio&page=1&size=10&sort=situacao&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(3)))
            .andExpect(jsonPath("$.items[1].id", equalTo(1)))
            .andExpect(jsonPath("$.items[0].nome", equalTo("convenio correlacao")))
            .andExpect(jsonPath("$.items[0].concedente.nome", equalTo("C concedente")))
            .andExpect(jsonPath("$.items[0].concedente.id", equalTo(3)));
    }

    @Test
    public void deveBuscarConveniosComConteudoConcedente() throws Exception{
        mockMvc.perform(
            get("/convenios?conteudo=nome&page=1&size=10&sort=situacao&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(1)))
            .andExpect(jsonPath("$.items[1].id", equalTo(2)))
            .andExpect(jsonPath("$.items[0].nome", equalTo("convenio alagoas")))
            .andExpect(jsonPath("$.items[0].concedente.nome", equalTo("B nome concedente")))
            .andExpect(jsonPath("$.items[0].concedente.id", equalTo(1)));
    }

    @Test
    public void deveBuscarConveniosSortCadastro() throws Exception{
        mockMvc.perform(
            get("/convenios?page=1&size=10&sort=situacao&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(3)))
            .andExpect(jsonPath("$.items[1].id", equalTo(1)))
            .andExpect(jsonPath("$.items[2].id", equalTo(2)));
    }

    @Test
    public void deveBuscarConveniosSortEdicao() throws Exception{
        mockMvc.perform(
            get("/convenios?page=1&size=10&sort=situacao&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(3)))
            .andExpect(jsonPath("$.items[1].id", equalTo(1)))
            .andExpect(jsonPath("$.items[2].id", equalTo(2)));
    }

    @Test
    public void deveBuscarApenasConveniosAtivos() throws Exception {
        mockMvc.perform(
            get("/convenios?conteudo=a&page=1&size=10&sort=nome&situacao=ATIVO&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.totalElements", equalTo(2)))
            .andExpect(jsonPath("$.items[0].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[1].situacao", equalTo("ATIVO")));
    }
}
