package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global;

import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarporid.BuscarReservaPorIdInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarporid.BuscarReservaPorIdOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarporid.BuscarReservaPorIdUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarporid.BuscarReservaPorIdUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarporid.converter.BuscarReservaPorIdOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarporid.exception.ReservaNaoEncontradaException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuscarReservaPorIdUseCaseTest {

    @Mock
    private ReservaDataProvider reservaDataProvider;

    @InjectMocks
    private BuscarReservaPorIdOutputDataConverter outputDataConverter;

    private BuscarReservaPorIdUseCase useCase;

    @Mock
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @Before
    public void setUp() {
        useCase = new BuscarReservaPorIdUseCaseImpl(
            reservaDataProvider,
            sessaoUsuarioDataProvider,
            outputDataConverter
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdNulo() {
        BuscarReservaPorIdInputData inputData = new BuscarReservaPorIdInputData();

        useCase.executar(inputData);
    }

    @Test(expected = ReservaNaoEncontradaException.class)
    public void deveFalharQuandoReservaNaoEncontrada() {
        BuscarReservaPorIdInputData inputData = new BuscarReservaPorIdInputData(2L);

        when(sessaoUsuarioDataProvider.get())
                .thenReturn(SessaoUsuario.builder()
                        .id(1L)
                        .permissoes(List.of("Mobiliario.Normal"))
                        .build()
                );
        useCase.executar(inputData);
    }

    @Test
    public void deveBuscarDadosReserva() {
        BuscarReservaPorIdInputData inputData = new BuscarReservaPorIdInputData(2L);

        when(sessaoUsuarioDataProvider.get())
                .thenReturn(SessaoUsuario.builder()
                        .id(1L)
                        .permissoes(List.of("Mobiliario.Normal"))
                        .build()
                );

        when(reservaDataProvider.buscarPorId(anyLong(),any(Reserva.Filtro.class)))
            .thenReturn(Optional.of(
                Reserva
                    .builder()
                    .id(2L)
                    .codigo("00002")
                    .situacao(Reserva.Situacao.EM_ELABORACAO)
                    .preenchimento(Reserva.Preenchimento.AUTOMATICO)
                    .quantidadeReservada(50L)
                    .quantidadeRestante(50L)
                    .numeroInicio(1L)
                    .numeroFim(50L)
                    .build()
            ));

        BuscarReservaPorIdOutputData outputData = useCase.executar(inputData);

        assertEquals(Long.valueOf(2), outputData.getId());
        assertEquals("00002", outputData.getCodigo());
        assertEquals("EM_ELABORACAO", outputData.getSituacao());
        assertEquals("AUTOMATICO", outputData.getPreenchimento());
        assertEquals(Long.valueOf(50), outputData.getQuantidadeReservada());
        assertEquals(Long.valueOf(50), outputData.getQuantidadeRestante());
        assertEquals(Long.valueOf(1), outputData.getNumeroInicio());
        assertEquals(Long.valueOf(50), outputData.getNumeroFim());
    }

}
