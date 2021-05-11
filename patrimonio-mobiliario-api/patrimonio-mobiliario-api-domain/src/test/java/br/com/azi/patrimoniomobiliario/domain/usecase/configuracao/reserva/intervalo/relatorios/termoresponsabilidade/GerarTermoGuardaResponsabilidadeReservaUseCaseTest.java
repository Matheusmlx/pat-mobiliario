package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.entity.TermoGuardaResponsabilidadeReservaPatrimonial;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade.GerarTermoGuardaResponsabilidadeReservaInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade.GerarTermoGuardaResponsabilidadeReservaUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade.GerarTermoGuardaResponsabilidadeReservaUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade.converter.GerarTermoGuardaResponsabilidadeReservaOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade.exception.IntervaloNaoPertenceReservaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade.exception.ReservaIntervaloNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade.exception.ReservaIntervaloNaoFinalizadaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade.exception.ReservaNaoEncontradaException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GerarTermoGuardaResponsabilidadeReservaUseCaseTest {

    private static final LocalDate LOCAL_DATE = LocalDate.of(2021, 1, 12);

    @Mock
    private ReservaDataProvider reservaDataProvider;

    @Mock
    private ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    @Mock
    private SistemaDeRelatoriosIntegration sistemaDeRelatoriosIntegration;

    @InjectMocks
    private GerarTermoGuardaResponsabilidadeReservaOutputDataConverter outputDataConverter;

    private GerarTermoGuardaResponsabilidadeReservaUseCase useCase;

    private final Clock fixedClock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    @Before
    public void setUp() {
        useCase = new GerarTermoGuardaResponsabilidadeReservaUseCaseImpl(
            fixedClock,
            reservaDataProvider,
            reservaIntervaloDataProvider,
            sistemaDeRelatoriosIntegration,
            outputDataConverter
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdReservaNulo() {
        GerarTermoGuardaResponsabilidadeReservaInputData inputData = GerarTermoGuardaResponsabilidadeReservaInputData
            .builder()
            .reservaIntervaloId(2L)
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdReservaIntervaloNulo() {
        GerarTermoGuardaResponsabilidadeReservaInputData inputData = GerarTermoGuardaResponsabilidadeReservaInputData
            .builder()
            .reservaId(2L)
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = ReservaNaoEncontradaException.class)
    public void deveFalharQuandoReservaNaoEncotrada() {
        GerarTermoGuardaResponsabilidadeReservaInputData inputData = GerarTermoGuardaResponsabilidadeReservaInputData
            .builder()
            .reservaId(1L)
            .reservaIntervaloId(1L)
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = ReservaIntervaloNaoEncontradaException.class)
    public void deveFalharQuandoIntervaloNaoEncontrado() {
        GerarTermoGuardaResponsabilidadeReservaInputData inputData = GerarTermoGuardaResponsabilidadeReservaInputData
            .builder()
            .reservaId(1L)
            .reservaIntervaloId(1L)
            .build();

        when(reservaDataProvider.buscarPorId(anyLong()))
            .thenReturn(
                Optional.of(
                    Reserva
                        .builder()
                        .id(1L)
                        .build()
                )
            );

        useCase.executar(inputData);
    }

    @Test(expected = IntervaloNaoPertenceReservaException.class)
    public void deveFalharQuandoIntervaloNaoPertenceReserva() {
        GerarTermoGuardaResponsabilidadeReservaInputData inputData = GerarTermoGuardaResponsabilidadeReservaInputData
            .builder()
            .reservaId(1L)
            .reservaIntervaloId(1L)
            .build();

        when(reservaDataProvider.buscarPorId(anyLong()))
            .thenReturn(
                Optional.of(
                    Reserva
                        .builder()
                        .id(1L)
                        .build()
                )
            );

        when(reservaIntervaloDataProvider.buscarPorId(anyLong()))
            .thenReturn(
                Optional.of(
                    ReservaIntervalo
                        .builder()
                        .id(1L)
                        .reserva(
                            Reserva
                                .builder()
                                .id(2L)
                                .build()
                        )
                        .build()
                )
            );

        useCase.executar(inputData);
    }

    @Test(expected = ReservaIntervaloNaoFinalizadaException.class)
    public void deveFalharQuandoIntervaloNaoFinalizado() {
        GerarTermoGuardaResponsabilidadeReservaInputData inputData = GerarTermoGuardaResponsabilidadeReservaInputData
            .builder()
            .reservaId(1L)
            .reservaIntervaloId(1L)
            .build();

        Reserva reserva = Reserva
            .builder()
            .id(1L)
            .build();

        when(reservaDataProvider.buscarPorId(anyLong()))
            .thenReturn(
                Optional.of(reserva)
            );

        when(reservaIntervaloDataProvider.buscarPorId(anyLong()))
            .thenReturn(
                Optional.of(
                    ReservaIntervalo
                        .builder()
                        .id(1L)
                        .reserva(reserva)
                        .situacao(ReservaIntervalo.Situacao.EM_ELABORACAO)
                        .build()
                )
            );

        useCase.executar(inputData);
    }

    @Test
    public void deveGerarTermoGuardaResposabilidadeReserva() {
        GerarTermoGuardaResponsabilidadeReservaInputData inputData = GerarTermoGuardaResponsabilidadeReservaInputData
            .builder()
            .reservaId(1L)
            .reservaIntervaloId(1L)
            .build();

        Reserva reserva = Reserva
            .builder()
            .id(1L)
            .build();

        TermoGuardaResponsabilidadeReservaPatrimonial termoResponsabilidade = TermoGuardaResponsabilidadeReservaPatrimonial
            .builder()
            .numeroTermo("000001/2021")
            .orgao("DPGE - Defensoria Pública Geral do Estado")
            .codigo("00001")
            .dataCriacao("04/05/2021")
            .preenchimento("Automático")
            .quantidade(12L)
            .intervalo("1 - 12")
            .build();

        when(reservaDataProvider.buscarPorId(anyLong()))
            .thenReturn(
                Optional.of(reserva)
            );

        when(reservaIntervaloDataProvider.buscarPorId(anyLong()))
            .thenReturn(
                Optional.of(
                    ReservaIntervalo
                        .builder()
                        .id(1L)
                        .reserva(reserva)
                        .codigo("00001")
                        .situacao(ReservaIntervalo.Situacao.FINALIZADO)
                        .dataFinalizacao(LocalDateTime.of(2021, 5, 4, 0, 0, 0))
                        .preenchimento(ReservaIntervalo.Preenchimento.AUTOMATICO)
                        .quantidadeReservada(12L)
                        .numeroInicio(1L)
                        .numeroFim(12L)
                        .orgao(UnidadeOrganizacional
                            .builder()
                            .sigla("DPGE")
                            .nome("Defensoria Pública Geral do Estado")
                            .build())
                        .build()
                )
            );

        byte[] conteudo = new byte[]{1, 2};

        when(sistemaDeRelatoriosIntegration.gerarTermoGuardaResponsabilidadeReservaPatrimonial(any(TermoGuardaResponsabilidadeReservaPatrimonial.class)))
            .thenReturn(Arquivo
                .builder()
                .contentType("application/pdf")
                .nome(String.format("%s.pdf", "termoguardaresponsabilidadereserva"))
                .content(conteudo)
                .build());

        useCase.executar(inputData);

        verify(sistemaDeRelatoriosIntegration, times(1)).gerarTermoGuardaResponsabilidadeReservaPatrimonial(termoResponsabilidade);
    }

}
