package br.com.azi.patrimoniomobiliario.integrationtest.configuracao.reserva;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.listagem.BuscarReservasOutputData;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.AuthenticationHelper;
import br.com.azi.patrimoniomobiliario.integrationtest.helper.JsonHelper;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;
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

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Sql({"/datasets/usuario.sql", "/datasets/configuracao/reserva/buscar-reservas.sql"})
@Transactional
public class BuscarReservasTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void deveBuscarReservasRespeitandoCriterioDeAcessoDoUsuarioEOrdenacaoPorSituacaoEDataAtualizacao() throws Exception {
        BuscarReservasOutputData responseEsperado = BuscarReservasOutputData.builder()
            .items(List.of(
                BuscarReservasOutputData.Item.builder()
                    .id(1L)
                    .codigo("00001")
                    .dataCriacao(DateValidate.formatarData(LocalDateTime.of(2021,3,15,0,0,0)))
                    .quantidadeReservada(50L)
                    .quantidadeRestante(50L)
                    .orgaos(null)
                    .situacao("EM_ELABORACAO")
                    .numeroInicio(1L)
                    .numeroFim(50L)
                    .build(),
                BuscarReservasOutputData.Item.builder()
                    .id(3L)
                    .codigo("00003")
                    .dataCriacao(DateValidate.formatarData(LocalDateTime.of(2021,4,15,0,0,0)))
                    .quantidadeReservada(10L)
                    .quantidadeRestante(7L)
                    .orgaos(Set.of("ADMIN"))
                    .situacao("PARCIAL")
                    .numeroInicio(101L)
                    .numeroFim(110L)
                    .build(),
                BuscarReservasOutputData.Item.builder()
                    .id(2L)
                    .codigo("00002")
                    .dataCriacao(DateValidate.formatarData(LocalDateTime.of(2021,4,15,0,0,0)))
                    .quantidadeReservada(50L)
                    .quantidadeRestante(0L)
                    .orgaos(new LinkedHashSet<>(List.of("ADMIN", "EDUCA")))
                    .situacao("FINALIZADO")
                    .numeroInicio(51L)
                    .numeroFim(100L)
                    .build()
            ))
            .totalPages(1L)
            .totalElements(3L)
            .build();

        String responseContent = mockMvc.perform(
            get("/configuracao/reservas?page=1&size=10")
                .headers(AuthenticationHelper.mockHeaders("usuario", "1", "ESTRUTURA_ORGANIZACIONAL"))
                .header("az-roles", "Mobiliario.Normal")
                .cookie(AuthenticationHelper.getCookies())
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is2xxSuccessful())
            .andReturn()
            .getResponse()
            .getContentAsString();

        BuscarReservasOutputData response = JsonHelper.fromJson(responseContent, BuscarReservasOutputData.class);

        assertEquals(responseEsperado, response);
    }
}
