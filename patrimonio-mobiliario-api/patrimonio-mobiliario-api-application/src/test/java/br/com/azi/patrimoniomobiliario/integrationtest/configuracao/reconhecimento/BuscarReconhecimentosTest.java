package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.reconhecimento;

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
@Sql({"/datasets/configuracao/reconhecimento/buscar-reconhecimentos.sql"})
@Transactional
public class BuscarReconhecimentosTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarReconhecimentosSortNomeDESC() throws Exception {
        mockMvc.perform(
            get("/reconhecimentos?page=1&size=10&sort=nome&direction=DESC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(3)))
            .andExpect(jsonPath("$.items[0].nome", equalTo("DOAÇÃO COMPRAS")))
            .andExpect(jsonPath("$.items[0].empenho", equalTo(true)))
            .andExpect(jsonPath("$.items[1].id", equalTo(1)))
            .andExpect(jsonPath("$.items[1].nome", equalTo("COMPRAS")))
            .andExpect(jsonPath("$.items[1].notaFiscal", equalTo(true)))
            .andExpect(jsonPath("$.items[1].empenho", equalTo(false)))
            .andExpect(jsonPath("$.items[2].id", equalTo(2)))
            .andExpect(jsonPath("$.items[2].nome", equalTo("DOAÇÃO")));
    }

    @Test
    public void deveBuscarReconhecimentosSortNomeASC() throws Exception {
        mockMvc.perform(
            get("/reconhecimentos?page=1&size=10&sort=nome&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(1)))
            .andExpect(jsonPath("$.items[0].nome", equalTo("COMPRAS")))
            .andExpect(jsonPath("$.items[1].id", equalTo(3)))
            .andExpect(jsonPath("$.items[1].nome", equalTo("DOAÇÃO COMPRAS")))
            .andExpect(jsonPath("$.items[2].id", equalTo(2)))
            .andExpect(jsonPath("$.items[2].nome", equalTo("DOAÇÃO")));
    }

    @Test
    public void deveBuscarReconhecimentosComConteudoNome() throws Exception {
        mockMvc.perform(
            get("/reconhecimentos?conteudo=doação&page=1&size=10&sort=situacao&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(3)))
            .andExpect(jsonPath("$.items[0].nome", equalTo("DOAÇÃO COMPRAS")))
            .andExpect(jsonPath("$.items[1].id", equalTo(2)))
            .andExpect(jsonPath("$.items[1].nome", equalTo("DOAÇÃO")));
    }

    @Test
    public void deveBuscarReconhecimentosComConteudoSituacao() throws Exception {
        mockMvc.perform(
            get("/reconhecimentos?conteudo=inativo&page=1&size=10&sort=situacao&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(2)))
            .andExpect(jsonPath("$.items[0].nome", equalTo("DOAÇÃO")));
    }

    @Test
    public void deveBuscarReconhecimentosComConteudoExecOrcamentaria() throws Exception {
        mockMvc.perform(
            get("/reconhecimentos?conteudo=nao&page=1&size=10&sort=situacao&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(3)))
            .andExpect(jsonPath("$.items[0].nome", equalTo("DOAÇÃO COMPRAS")))
            .andExpect(jsonPath("$.items[1].id", equalTo(2)))
            .andExpect(jsonPath("$.items[1].nome", equalTo("DOAÇÃO")));
    }

    @Test
    public void deveBuscarReconhecimentosComSortSituacaoASC() throws Exception {
        mockMvc.perform(
            get("/reconhecimentos?page=1&size=10&sort=situacao&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[1].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[2].situacao", equalTo("INATIVO")));
    }

    @Test
    public void deveBuscarReconhecimentosComSortSituacaoDESC() throws Exception {
        mockMvc.perform(
            get("/reconhecimentos?page=1&size=10&sort=situacao&direction=DESC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].situacao", equalTo("INATIVO")))
            .andExpect(jsonPath("$.items[1].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[2].situacao", equalTo("ATIVO")));
    }

    @Test
    public void deveBuscarReconhecimentosComSortExecucaoOrcamentariaASC() throws Exception {
        mockMvc.perform(
            get("/reconhecimentos?page=1&size=10&sort=execucaoOrcamentaria&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].execucaoOrcamentaria", equalTo(false)))
            .andExpect(jsonPath("$.items[1].execucaoOrcamentaria", equalTo(true)))
            .andExpect(jsonPath("$.items[2].execucaoOrcamentaria", equalTo(false)))
            .andExpect(jsonPath("$.items[0].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[1].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[2].situacao", equalTo("INATIVO")));
    }

    @Test
    public void deveBuscarReconhecimentosComSortExecucaoOrcamentariaDESC() throws Exception {
        mockMvc.perform(
            get("/reconhecimentos?page=1&size=10&sort=execucaoOrcamentaria&direction=DESC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].execucaoOrcamentaria", equalTo(true)))
            .andExpect(jsonPath("$.items[1].execucaoOrcamentaria", equalTo(false)))
            .andExpect(jsonPath("$.items[2].execucaoOrcamentaria", equalTo(false)))
            .andExpect(jsonPath("$.items[0].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[1].situacao", equalTo("ATIVO")))
            .andExpect(jsonPath("$.items[2].situacao", equalTo("INATIVO")));
    }

    @Test
    public void deveBuscarReconhecimentosSortCadastro() throws Exception {
        mockMvc.perform(
            get("/reconhecimentos?page=1&size=10&sort=situacao&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(1)))
            .andExpect(jsonPath("$.items[1].id", equalTo(3)))
            .andExpect(jsonPath("$.items[2].id", equalTo(2)));
    }

    @Test
    public void deveBuscarReconhecimentosSortEdicao() throws Exception {
        mockMvc.perform(
            get("/reconhecimentos?page=1&size=10&sort=situacao&direction=ASC")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id", equalTo(1)))
            .andExpect(jsonPath("$.items[1].id", equalTo(3)))
            .andExpect(jsonPath("$.items[2].id", equalTo(2)));
    }
}
