package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.listagem.BuscarReservasInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.listagem.BuscarReservasOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.listagem.BuscarReservasUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.listagem.BuscarReservasUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.listagem.converter.BuscarReservasFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.listagem.converter.BuscarReservasOutputDataConverter;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuscarReservasTest {

    private static final LocalDate LOCAL_DATE = LocalDate.of(2021, 4, 15);

    @Mock
    private ReservaDataProvider reservaDataProvider;

    @Mock
    private ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    @Mock
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    private BuscarReservasUseCase useCase;

    @Before
    public void inicializarUseCase() {
        useCase = new BuscarReservasUseCaseImpl(
            reservaDataProvider,
            reservaIntervaloDataProvider,
            sessaoUsuarioDataProvider,
            new BuscarReservasFiltroConverter(),
            new BuscarReservasOutputDataConverter()
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoPageNaoForInformado() {
        useCase.executar(new BuscarReservasInputData());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoSizeNaoForInformado() {
        useCase.executar(BuscarReservasInputData.builder().page(1L).build());
    }

    @Test
    public void deveChamarAConsultaComOFiltroCorreto() {
        final BuscarReservasInputData inputData = BuscarReservasInputData.builder()
            .page(1L)
            .size(10L)
            .build();

        final Reserva.Filtro filtro = Reserva.Filtro.builder()
            .usuarioId(1L)
            .funcoes(List.of("Mobiliario.Normal", "Mobiliario.Consulta"))
            .build();
        filtro.setPage(0L);
        filtro.setSize(10L);

        when(sessaoUsuarioDataProvider.get())
            .thenReturn(SessaoUsuario.builder()
                .id(1L)
                .permissoes(List.of("Mobiliario.Normal"))
                .build()
            );

        when(reservaDataProvider.buscarReservas(any(Reserva.Filtro.class)))
            .thenReturn(ListaPaginada.<Reserva>builder()
                .items(List.of(
                    Reserva.builder()
                        .id(1L)
                        .codigo("00001")
                        .situacao(Reserva.Situacao.EM_ELABORACAO)
                        .numeroInicio(1L)
                        .numeroFim(100L)
                        .quantidadeReservada(100L)
                        .quantidadeRestante(100L)
                        .dataCriacao(LOCAL_DATE.atStartOfDay())
                        .build()
                ))
                .totalElements(1L)
                .totalPages(1L)
                .build()
            );

        when(reservaIntervaloDataProvider.buscarPorReserva(anyLong())).thenReturn(Collections.emptyList());

        useCase.executar(inputData);

        verify(sessaoUsuarioDataProvider, times(1)).get();
        verify(reservaDataProvider, times(1)).buscarReservas(filtro);
        verify(reservaIntervaloDataProvider, times(1)).buscarPorReserva(1L);
    }

    @Test
    public void deveRetornarAsReservasSemIntervalosCadastrados() {
        final BuscarReservasInputData inputData = BuscarReservasInputData.builder()
            .page(1L)
            .size(10L)
            .build();

        final Reserva.Filtro filtro = Reserva.Filtro.builder()
            .usuarioId(1L)
            .funcoes(List.of("Mobiliario.Normal", "Mobiliario.Consulta"))
            .build();
        filtro.setPage(0L);
        filtro.setSize(10L);

        final BuscarReservasOutputData outputDataEsperado = BuscarReservasOutputData.builder()
            .items(List.of(
                BuscarReservasOutputData.Item.builder()
                    .id(1L)
                    .codigo("00001")
                    .dataCriacao(DateValidate.formatarData(LOCAL_DATE.atStartOfDay()))
                    .quantidadeReservada(100L)
                    .quantidadeRestante(100L)
                    .orgaos(null)
                    .situacao("EM_ELABORACAO")
                    .numeroInicio(1L)
                    .numeroFim(100L)
                    .build()
            ))
            .totalElements(1L)
            .totalPages(1L)
            .build();

        when(sessaoUsuarioDataProvider.get())
            .thenReturn(SessaoUsuario.builder()
                .id(1L)
                .permissoes(List.of("Mobiliario.Normal"))
                .build()
            );

        when(reservaDataProvider.buscarReservas(any(Reserva.Filtro.class)))
            .thenReturn(ListaPaginada.<Reserva>builder()
                .items(List.of(
                    Reserva.builder()
                        .id(1L)
                        .codigo("00001")
                        .situacao(Reserva.Situacao.EM_ELABORACAO)
                        .numeroInicio(1L)
                        .numeroFim(100L)
                        .quantidadeReservada(100L)
                        .quantidadeRestante(100L)
                        .dataCriacao(LOCAL_DATE.atStartOfDay())
                        .build()
                ))
                .totalElements(1L)
                .totalPages(1L)
                .build()
            );

        when(reservaIntervaloDataProvider.buscarPorReserva(anyLong())).thenReturn(Collections.emptyList());

        final BuscarReservasOutputData outputData = useCase.executar(inputData);

        assertEquals(outputDataEsperado, outputData);
        verify(sessaoUsuarioDataProvider, times(1)).get();
        verify(reservaDataProvider, times(1)).buscarReservas(filtro);
        verify(reservaIntervaloDataProvider, times(1)).buscarPorReserva(1L);
    }

    @Test
    public void deveRetornarAsReservasComIntervalosCadastrados() {
        final BuscarReservasInputData inputData = BuscarReservasInputData.builder()
            .page(1L)
            .size(10L)
            .build();

        final Reserva.Filtro filtro = Reserva.Filtro.builder()
            .usuarioId(1L)
            .funcoes(List.of("Mobiliario.Normal", "Mobiliario.Consulta"))
            .build();
        filtro.setPage(0L);
        filtro.setSize(10L);

        final BuscarReservasOutputData outputDataEsperado = BuscarReservasOutputData.builder()
            .items(List.of(
                BuscarReservasOutputData.Item.builder()
                    .id(1L)
                    .codigo("00001")
                    .dataCriacao(DateValidate.formatarData(LOCAL_DATE.atStartOfDay()))
                    .quantidadeReservada(100L)
                    .quantidadeRestante(100L)
                    .orgaos(Set.of("DPGE"))
                    .situacao("EM_ELABORACAO")
                    .numeroInicio(1L)
                    .numeroFim(100L)
                    .build()
            ))
            .totalElements(1L)
            .totalPages(1L)
            .build();

        when(sessaoUsuarioDataProvider.get())
            .thenReturn(SessaoUsuario.builder()
                .id(1L)
                .permissoes(List.of("Mobiliario.Normal"))
                .build()
            );

        when(reservaDataProvider.buscarReservas(any(Reserva.Filtro.class)))
            .thenReturn(ListaPaginada.<Reserva>builder()
                .items(List.of(
                    Reserva.builder()
                        .id(1L)
                        .codigo("00001")
                        .situacao(Reserva.Situacao.EM_ELABORACAO)
                        .numeroInicio(1L)
                        .numeroFim(100L)
                        .quantidadeReservada(100L)
                        .quantidadeRestante(100L)
                        .dataCriacao(LOCAL_DATE.atStartOfDay())
                        .build()
                ))
                .totalElements(1L)
                .totalPages(1L)
                .build()
            );

        when(reservaIntervaloDataProvider.buscarPorReserva(anyLong()))
            .thenReturn(Collections.singletonList(
                ReservaIntervalo.builder()
                    .id(1L)
                    .orgao(UnidadeOrganizacional.builder()
                        .id(1L)
                        .sigla("DPGE")
                        .build()
                    )
                    .build()
            ));

        final BuscarReservasOutputData outputData = useCase.executar(inputData);

        assertEquals(outputDataEsperado, outputData);
        verify(sessaoUsuarioDataProvider, times(1)).get();
        verify(reservaDataProvider, times(1)).buscarReservas(filtro);
        verify(reservaIntervaloDataProvider, times(1)).buscarPorReserva(1L);
    }

    @Test
    public void deveRetornarSiglaDoOrgaoSemRepeticaoQuandoOMesmoOrgaoTiverVariosIntervalosCadastrados() {
        final BuscarReservasInputData inputData = BuscarReservasInputData.builder()
            .page(1L)
            .size(10L)
            .build();

        final Reserva.Filtro filtro = Reserva.Filtro.builder()
            .usuarioId(1L)
            .funcoes(List.of("Mobiliario.Normal", "Mobiliario.Consulta"))
            .build();
        filtro.setPage(0L);
        filtro.setSize(10L);

        final BuscarReservasOutputData outputDataEsperado = BuscarReservasOutputData.builder()
            .items(List.of(
                BuscarReservasOutputData.Item.builder()
                    .id(1L)
                    .codigo("00001")
                    .dataCriacao(DateValidate.formatarData(LOCAL_DATE.atStartOfDay()))
                    .quantidadeReservada(100L)
                    .quantidadeRestante(100L)
                    .orgaos(new LinkedHashSet<>(List.of("DPGE", "SAD")))
                    .situacao("EM_ELABORACAO")
                    .numeroInicio(1L)
                    .numeroFim(100L)
                    .build()
            ))
            .totalElements(1L)
            .totalPages(1L)
            .build();

        when(sessaoUsuarioDataProvider.get())
            .thenReturn(SessaoUsuario.builder()
                .id(1L)
                .permissoes(List.of("Mobiliario.Normal"))
                .build()
            );

        when(reservaDataProvider.buscarReservas(any(Reserva.Filtro.class)))
            .thenReturn(ListaPaginada.<Reserva>builder()
                .items(List.of(
                    Reserva.builder()
                        .id(1L)
                        .codigo("00001")
                        .situacao(Reserva.Situacao.EM_ELABORACAO)
                        .numeroInicio(1L)
                        .numeroFim(100L)
                        .quantidadeReservada(100L)
                        .quantidadeRestante(100L)
                        .dataCriacao(LOCAL_DATE.atStartOfDay())
                        .build()
                ))
                .totalElements(1L)
                .totalPages(1L)
                .build()
            );

        when(reservaIntervaloDataProvider.buscarPorReserva(anyLong()))
            .thenReturn(List.of(
                ReservaIntervalo.builder()
                    .id(1L)
                    .orgao(UnidadeOrganizacional.builder()
                        .id(1L)
                        .sigla("DPGE")
                        .build()
                    )
                    .build(),
                ReservaIntervalo.builder()
                    .id(2L)
                    .orgao(UnidadeOrganizacional.builder()
                        .id(1L)
                        .sigla("DPGE")
                        .build()
                    )
                    .build(),
                ReservaIntervalo.builder()
                    .id(3L)
                    .orgao(UnidadeOrganizacional.builder()
                        .id(2L)
                        .sigla("SAD")
                        .build()
                    )
                    .build()
            ));

        final BuscarReservasOutputData outputData = useCase.executar(inputData);

        assertEquals(outputDataEsperado, outputData);
        verify(sessaoUsuarioDataProvider, times(1)).get();
        verify(reservaDataProvider, times(1)).buscarReservas(filtro);
        verify(reservaIntervaloDataProvider, times(1)).buscarPorReserva(1L);
    }
}
