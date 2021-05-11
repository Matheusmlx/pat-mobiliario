package br.com.azi.patrimoniomobiliario.integrationtest.incorporacao.item;

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
@Sql("/datasets/incorporacao/item/buscar-itens-incorporacao.sql")
@Transactional
public class BuscarItensIncorporacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarItensIncorporacaoSortCodigoASC () throws Exception{
        mockMvc.perform(
            get("/patrimonios/incorporacao/itens/1?page=1&size=10&sort=codigo&direction=ASC")
                .headers(AuthenticationHelper.getHeaders())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id",equalTo(2)))
            .andExpect(jsonPath("$.items[1].id",equalTo(4)))
            .andExpect(jsonPath("$.items[2].id",equalTo(1)))
            .andExpect(jsonPath("$.items[0].situacao",equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.items[1].situacao",equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.items[2].situacao",equalTo("FINALIZADO")));
    }

    @Test
    public void deveBuscarItensIncorporacaoSortCodigoDESC () throws Exception{
        mockMvc.perform(
            get("/patrimonios/incorporacao/itens/1?page=1&size=10&sort=codigo&direction=DESC")
                .headers(AuthenticationHelper.getHeaders())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id",equalTo(4)))
            .andExpect(jsonPath("$.items[1].id",equalTo(2)))
            .andExpect(jsonPath("$.items[2].id",equalTo(1)))
            .andExpect(jsonPath("$.items[0].situacao",equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.items[1].situacao",equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.items[2].situacao",equalTo("FINALIZADO")));
    }

    @Test
    public void deveBuscarItensIncorporacaoSortDescricaoASC () throws Exception{
        mockMvc.perform(
            get("/patrimonios/incorporacao/itens/1?page=1&size=10&sort=descricao&direction=ASC")
                .headers(AuthenticationHelper.getHeaders())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id",equalTo(2)))
            .andExpect(jsonPath("$.items[1].id",equalTo(4)))
            .andExpect(jsonPath("$.items[2].id",equalTo(1)))
            .andExpect(jsonPath("$.items[0].situacao",equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.items[1].situacao",equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.items[2].situacao",equalTo("FINALIZADO")));
    }

    @Test
    public void deveBuscarItensIncorporacaoSortDescricaoDESC () throws Exception{
        mockMvc.perform(
            get("/patrimonios/incorporacao/itens/1?page=1&size=10&sort=descricao&direction=DESC")
                .headers(AuthenticationHelper.getHeaders())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id",equalTo(4)))
            .andExpect(jsonPath("$.items[1].id",equalTo(2)))
            .andExpect(jsonPath("$.items[2].id",equalTo(1)))
            .andExpect(jsonPath("$.items[0].situacao",equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.items[1].situacao",equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.items[2].situacao",equalTo("FINALIZADO")));
    }

    @Test
    public void deveBuscarItensIncorporacaoSortQuantidadeASC () throws Exception{
        mockMvc.perform(
            get("/patrimonios/incorporacao/itens/1?page=1&size=10&sort=quantidade&direction=ASC")
                .headers(AuthenticationHelper.getHeaders())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id",equalTo(2)))
            .andExpect(jsonPath("$.items[1].id",equalTo(4)))
            .andExpect(jsonPath("$.items[2].id",equalTo(1)))
            .andExpect(jsonPath("$.items[0].situacao",equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.items[1].situacao",equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.items[2].situacao",equalTo("FINALIZADO")));
    }

    @Test
    public void deveBuscarItensIncorporacaoSortQuantidadeDESC () throws Exception{
        mockMvc.perform(
            get("/patrimonios/incorporacao/itens/1?page=1&size=10&sort=quantidade&direction=DESC")
                .headers(AuthenticationHelper.getHeaders())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id",equalTo(2)))
            .andExpect(jsonPath("$.items[1].id",equalTo(4)))
            .andExpect(jsonPath("$.items[2].id",equalTo(1)))
            .andExpect(jsonPath("$.items[0].situacao",equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.items[1].situacao",equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.items[2].situacao",equalTo("FINALIZADO")));
    }

    @Test
    public void deveBuscarItensIncorporacaoSortValorTotalASC () throws Exception{
        mockMvc.perform(
            get("/patrimonios/incorporacao/itens/1?page=1&size=10&sort=valorTotal&direction=ASC")
                .headers(AuthenticationHelper.getHeaders())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id",equalTo(2)))
            .andExpect(jsonPath("$.items[1].id",equalTo(4)))
            .andExpect(jsonPath("$.items[2].id",equalTo(1)))
            .andExpect(jsonPath("$.items[0].situacao",equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.items[1].situacao",equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.items[2].situacao",equalTo("FINALIZADO")));
    }

    @Test
    public void deveBuscarItensIncorporacaoSortValorTotalDESC () throws Exception{
        mockMvc.perform(
            get("/patrimonios/incorporacao/itens/1?page=1&size=10&sort=valorTotal&direction=DESC")
                .headers(AuthenticationHelper.getHeaders())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items[0].id",equalTo(2)))
            .andExpect(jsonPath("$.items[1].id",equalTo(4)))
            .andExpect(jsonPath("$.items[2].id",equalTo(1)))
            .andExpect(jsonPath("$.items[0].situacao",equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.items[1].situacao",equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.items[2].situacao",equalTo("FINALIZADO")));
    }

    @Test
    public void deveBuscarItensIncorporacaoConteudoCodigo() throws Exception{
        mockMvc.perform(
            get("/patrimonios/incorporacao/itens/1?conteudo=123456&page=1&size=10&sort=codigo&direction=ASC")
                .headers(AuthenticationHelper.getHeaders())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items.length()",equalTo(1)))
            .andExpect(jsonPath("$.items[0].id",equalTo(1)))
            .andExpect(jsonPath("$.items[0].codigo",equalTo("123456")));
    }

    @Test
    public void deveBuscarItensIncorporacaoConteudoDescricao() throws Exception{
        mockMvc.perform(
            get("/patrimonios/incorporacao/itens/2?conteudo=descricao 2&page=1&size=10&sort=codigo&direction=ASC")
                .headers(AuthenticationHelper.getHeaders())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items.length()",equalTo(1)))
            .andExpect(jsonPath("$.items[0].id",equalTo(3)))
            .andExpect(jsonPath("$.items[0].descricao",equalTo("descricao 2")));
    }

    @Test
    public void deveBuscarItensIncorporacaoComTodosAtributos() throws Exception{
        mockMvc.perform(
            get("/patrimonios/incorporacao/itens/2?conteudo=345678&page=1&size=10&sort=codigo&direction=ASC")
                .headers(AuthenticationHelper.getHeaders())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.items.length()",equalTo(1)))
            .andExpect(jsonPath("$.items[0].id",equalTo(3)))
            .andExpect(jsonPath("$.items[0].codigo",equalTo("345678")))
            .andExpect(jsonPath("$.items[0].descricao",equalTo("descricao 2")))
            .andExpect(jsonPath("$.items[0].valorTotal",equalTo(15.0)))
            .andExpect(jsonPath("$.items[0].quantidade",equalTo(2)))
            .andExpect(jsonPath("$.items[0].situacao",equalTo("EM_ELABORACAO")))
            .andExpect(jsonPath("$.items[0].tipoBem",equalTo(null)));
    }
}
