package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global;

import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.EditarReservaPatrimonialInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.EditarReservaPatrimonialOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.EditarReservaPatrimonialUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.EditarReservaPatrimonialUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.converter.EditarReservaPatrimonialOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.exception.IntervaloEmUsoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.exception.IntervaloInvalidoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.exception.IntervaloNaoInformadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.exception.PreenchimentoInvalidoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.exception.QuantidadeReservadaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.exception.ReservaNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.exception.ReservaNaoPodeSerEditadaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.exception.SituacaoReservaException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EditarReservaUseCaseTest {

    private static final LocalDate LOCAL_DATE = LocalDate.of(2021, 4, 23);

    @Mock
    private ReservaDataProvider reservaDataProvider;

    @Mock
    private ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    @InjectMocks
    private EditarReservaPatrimonialOutputDataConverter outputDataConverter;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private EditarReservaPatrimonialUseCase useCase;

    @Before
    public void setUp() {
        useCase = new EditarReservaPatrimonialUseCaseImpl(
            reservaDataProvider,
            reservaIntervaloDataProvider,
            patrimonioDataProvider,
            sessaoUsuarioDataProvider,
            outputDataConverter
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdForNulo() {
        EditarReservaPatrimonialInputData inputData = new EditarReservaPatrimonialInputData();

        useCase.executar(inputData);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoTipoPreenchimentoNulo() {
        EditarReservaPatrimonialInputData inputData = EditarReservaPatrimonialInputData
            .builder()
            .id(2L)
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = PreenchimentoInvalidoException.class)
    public void deveFalharSeTipoPreenchimentoInvalido() {
        EditarReservaPatrimonialInputData inputData = EditarReservaPatrimonialInputData
            .builder()
            .id(2L)
            .preenchimento("AUTOMATIC")
            .build();

        Reserva reserva = Reserva
            .builder()
            .id(2L)
            .codigo("00001")
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .preenchimento(Reserva.Preenchimento.AUTOMATICO)
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .quantidadeReservada(50L)
            .quantidadeRestante(50L)
            .numeroInicio(1L)
            .numeroFim(1L)
            .build();

        when(sessaoUsuarioDataProvider.get())
            .thenReturn(SessaoUsuario.builder()
                .id(1L)
                .permissoes(List.of("Mobiliario.Normal"))
                .build()
            );

        when(reservaDataProvider.buscarPorId(anyLong(), any(Reserva.Filtro.class)))
            .thenReturn(Optional.of(reserva));

        when(reservaIntervaloDataProvider.existeIntervalosPorReserva(any(Reserva.class)))
            .thenReturn(false);

        useCase.executar(inputData);
    }

    @Test(expected = ReservaNaoEncontradaException.class)
    public void deveFalharSeReservaNaoEncontrada() {
        EditarReservaPatrimonialInputData inputData = EditarReservaPatrimonialInputData
            .builder()
            .id(2L)
            .preenchimento("AUTOMATICO")
            .build();

        when(sessaoUsuarioDataProvider.get())
            .thenReturn(SessaoUsuario.builder()
                .id(1L)
                .permissoes(List.of("Mobiliario.Normal"))
                .build()
            );

        useCase.executar(inputData);
    }

    @Test(expected = ReservaNaoPodeSerEditadaException.class)
    public void deveFalharSeReservaNaoPodeSerAlterada() {
        EditarReservaPatrimonialInputData inputData = EditarReservaPatrimonialInputData
            .builder()
            .id(2L)
            .preenchimento("AUTOMATICO")
            .build();

        Reserva reserva = Reserva
            .builder()
            .id(2L)
            .codigo("00001")
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .preenchimento(Reserva.Preenchimento.AUTOMATICO)
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .quantidadeReservada(50L)
            .quantidadeRestante(50L)
            .numeroInicio(1L)
            .numeroFim(1L)
            .build();

        when(sessaoUsuarioDataProvider.get())
            .thenReturn(SessaoUsuario.builder()
                .id(1L)
                .permissoes(List.of("Mobiliario.Normal"))
                .build()
            );

        when(reservaDataProvider.buscarPorId(anyLong(), any(Reserva.Filtro.class)))
            .thenReturn(Optional.of(reserva));

        when(reservaIntervaloDataProvider.existeIntervalosPorReserva(any(Reserva.class)))
            .thenReturn(true);

        useCase.executar(inputData);
    }

    @Test(expected = SituacaoReservaException.class)
    public void deveFalharQuandoSituacaoReservaNaoEstarEmElaboracao() {
        EditarReservaPatrimonialInputData inputData = EditarReservaPatrimonialInputData
            .builder()
            .id(2L)
            .preenchimento("AUTOMATICO")
            .build();

        Reserva reserva = Reserva
            .builder()
            .id(2L)
            .codigo("00001")
            .situacao(Reserva.Situacao.PARCIAL)
            .preenchimento(Reserva.Preenchimento.AUTOMATICO)
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .quantidadeReservada(50L)
            .quantidadeRestante(50L)
            .numeroInicio(1L)
            .numeroFim(1L)
            .build();

        when(sessaoUsuarioDataProvider.get())
            .thenReturn(SessaoUsuario.builder()
                .id(1L)
                .permissoes(List.of("Mobiliario.Normal"))
                .build()
            );

        when(reservaDataProvider.buscarPorId(anyLong(), any(Reserva.Filtro.class)))
            .thenReturn(Optional.of(reserva));

        when(reservaIntervaloDataProvider.existeIntervalosPorReserva(any(Reserva.class)))
            .thenReturn(false);

        useCase.executar(inputData);
    }

    @Test
    public void deveFalharQuandoFaltaCamposPreenchimentoAutomatico() {
        EditarReservaPatrimonialInputData inputData = EditarReservaPatrimonialInputData
            .builder()
            .id(2L)
            .preenchimento("AUTOMATICO")
            .build();

        Reserva reserva = Reserva
            .builder()
            .id(2L)
            .codigo("00002")
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .preenchimento(Reserva.Preenchimento.AUTOMATICO)
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .quantidadeReservada(50L)
            .quantidadeRestante(50L)
            .numeroInicio(1L)
            .numeroFim(1L)
            .build();

        when(sessaoUsuarioDataProvider.get())
            .thenReturn(SessaoUsuario.builder()
                .id(1L)
                .permissoes(List.of("Mobiliario.Normal"))
                .build()
            );

        when(reservaDataProvider.buscarPorId(anyLong(), any(Reserva.Filtro.class)))
            .thenReturn(Optional.of(reserva));

        when(reservaIntervaloDataProvider.existeIntervalosPorReserva(any(Reserva.class)))
            .thenReturn(false);

        exception.expect(QuantidadeReservadaException.class);
        exception.expectMessage("A quantidade a ser reservada não foi informada");

        useCase.executar(inputData);
    }

    @Test
    public void deveFalharQuandoQuantidadeIgualZeroReservaAutomatica() {
        EditarReservaPatrimonialInputData inputData = EditarReservaPatrimonialInputData
            .builder()
            .id(2L)
            .preenchimento("AUTOMATICO")
            .quantidadeReservada(0L)
            .build();

        Reserva reserva = Reserva
            .builder()
            .id(2L)
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .preenchimento(Reserva.Preenchimento.AUTOMATICO)
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .quantidadeReservada(50L)
            .quantidadeRestante(50L)
            .numeroInicio(1L)
            .numeroFim(1L)
            .build();

        when(sessaoUsuarioDataProvider.get())
            .thenReturn(SessaoUsuario.builder()
                .id(1L)
                .permissoes(List.of("Mobiliario.Normal"))
                .build()
            );

        when(reservaDataProvider.buscarPorId(anyLong(), any(Reserva.Filtro.class)))
            .thenReturn(Optional.of(reserva));

        when(reservaIntervaloDataProvider.existeIntervalosPorReserva(any(Reserva.class)))
            .thenReturn(false);

        exception.expect(QuantidadeReservadaException.class);
        exception.expectMessage("A quantidade a ser reservada deve ser maior que 0");

        useCase.executar(inputData);
    }

    @Test
    public void deveRetornarReservaQuandoNenhumCampoAlteradoPreenchimentoAutomatico() {
        EditarReservaPatrimonialInputData inputData = EditarReservaPatrimonialInputData
            .builder()
            .id(2L)
            .preenchimento("AUTOMATICO")
            .quantidadeReservada(50L)
            .build();

        Reserva reserva = Reserva
            .builder()
            .id(2L)
            .codigo("00002")
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .preenchimento(Reserva.Preenchimento.AUTOMATICO)
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .quantidadeReservada(50L)
            .quantidadeRestante(50L)
            .numeroInicio(1L)
            .numeroFim(50L)
            .build();

        when(sessaoUsuarioDataProvider.get())
            .thenReturn(SessaoUsuario.builder()
                .id(1L)
                .permissoes(List.of("Mobiliario.Normal"))
                .build()
            );

        when(reservaDataProvider.buscarPorId(anyLong(), any(Reserva.Filtro.class)))
            .thenReturn(Optional.of(reserva));

        when(reservaIntervaloDataProvider.existeIntervalosPorReserva(any(Reserva.class)))
            .thenReturn(false);

        EditarReservaPatrimonialOutputData outputData = useCase.executar(inputData);

        assertEquals(Long.valueOf(2), outputData.getId());
        assertEquals("00002", outputData.getCodigo());
        assertEquals("AUTOMATICO", outputData.getPreenchimento());
        assertEquals("EM_ELABORACAO", outputData.getSituacao());
        assertEquals(Long.valueOf(50), outputData.getQuantidadeReservada());
        assertEquals(Long.valueOf(50), outputData.getQuantidadeRestante());
        assertEquals(Long.valueOf(1), outputData.getNumeroInicio());
        assertEquals(Long.valueOf(50), outputData.getNumeroFim());

        verify(reservaDataProvider).bloquearEntidade();
        verify(patrimonioDataProvider).bloquearEntidade();
    }

    @Test
    public void deveAtualizarReservaQuandoReservaAlterada() {
        EditarReservaPatrimonialInputData inputData = EditarReservaPatrimonialInputData
            .builder()
            .id(2L)
            .preenchimento("AUTOMATICO")
            .quantidadeReservada(51L)
            .build();

        Reserva reserva = Reserva
            .builder()
            .id(2L)
            .codigo("00002")
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .preenchimento(Reserva.Preenchimento.AUTOMATICO)
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .quantidadeReservada(50L)
            .quantidadeRestante(50L)
            .numeroInicio(1L)
            .numeroFim(50L)
            .build();

        Reserva reservaAlterada = Reserva
            .builder()
            .id(2L)
            .codigo("00002")
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .preenchimento(Reserva.Preenchimento.AUTOMATICO)
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .quantidadeReservada(51L)
            .quantidadeRestante(51L)
            .numeroInicio(1L)
            .numeroFim(51L)
            .build();

        when(sessaoUsuarioDataProvider.get())
            .thenReturn(SessaoUsuario.builder()
                .id(1L)
                .permissoes(List.of("Mobiliario.Normal"))
                .build()
            );

        when(reservaDataProvider.buscarPorId(anyLong(), any(Reserva.Filtro.class)))
            .thenReturn(Optional.of(reserva));

        when(reservaIntervaloDataProvider.existeIntervalosPorReserva(any(Reserva.class)))
            .thenReturn(false);

        when(reservaDataProvider.buscarReservaComMaiorNumeroDiferenteDe(any(Reserva.class)))
            .thenReturn(Optional.empty());

        when(patrimonioDataProvider.buscarPatrimonioComMaiorNumero())
            .thenReturn(Optional.empty());

        when(reservaDataProvider.salvar(any(Reserva.class)))
            .thenReturn(reservaAlterada);

        EditarReservaPatrimonialOutputData outputData = useCase.executar(inputData);

        verify(reservaDataProvider, times(1)).salvar(reservaAlterada);
        assertEquals(Long.valueOf(2), outputData.getId());
        assertEquals("00002", outputData.getCodigo());
        assertEquals("AUTOMATICO", outputData.getPreenchimento());
        assertEquals("EM_ELABORACAO", outputData.getSituacao());
        assertEquals(Long.valueOf(51), outputData.getQuantidadeReservada());
        assertEquals(Long.valueOf(51), outputData.getQuantidadeRestante());
        assertEquals(Long.valueOf(1), outputData.getNumeroInicio());
        assertEquals(Long.valueOf(51), outputData.getNumeroFim());

        verify(reservaDataProvider).bloquearEntidade();
        verify(patrimonioDataProvider).bloquearEntidade();
    }

    @Test
    public void deveAtualizarReservaQuandoReservaAlteradaComNumeroAposIntervalosExistentes() {
        EditarReservaPatrimonialInputData inputData = EditarReservaPatrimonialInputData
            .builder()
            .id(2L)
            .preenchimento("AUTOMATICO")
            .quantidadeReservada(51L)
            .build();

        Reserva reserva = Reserva
            .builder()
            .id(2L)
            .codigo("00002")
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .preenchimento(Reserva.Preenchimento.AUTOMATICO)
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .quantidadeReservada(50L)
            .quantidadeRestante(50L)
            .numeroInicio(1L)
            .numeroFim(50L)
            .build();

        Reserva reservaAlterada = Reserva
            .builder()
            .id(2L)
            .codigo("00002")
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .preenchimento(Reserva.Preenchimento.AUTOMATICO)
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .quantidadeReservada(51L)
            .quantidadeRestante(51L)
            .numeroInicio(52L)
            .numeroFim(102L)
            .build();

        when(sessaoUsuarioDataProvider.get())
            .thenReturn(SessaoUsuario.builder()
                .id(1L)
                .permissoes(List.of("Mobiliario.Normal"))
                .build()
            );

        when(reservaDataProvider.buscarPorId(anyLong(), any(Reserva.Filtro.class)))
            .thenReturn(Optional.of(reserva));

        when(reservaIntervaloDataProvider.existeIntervalosPorReserva(any(Reserva.class)))
            .thenReturn(false);

        when(reservaDataProvider.buscarReservaComMaiorNumeroDiferenteDe(any(Reserva.class)))
            .thenReturn(Optional.of(
                Reserva
                    .builder()
                    .numeroFim(51L)
                    .build()
            ));

        when(patrimonioDataProvider.buscarPatrimonioComMaiorNumero())
            .thenReturn(Optional.empty());

        when(reservaDataProvider.salvar(any(Reserva.class)))
            .thenReturn(reservaAlterada);

        EditarReservaPatrimonialOutputData outputData = useCase.executar(inputData);

        verify(reservaDataProvider, times(1)).salvar(reservaAlterada);
        assertEquals(Long.valueOf(2), outputData.getId());
        assertEquals("00002", outputData.getCodigo());
        assertEquals("AUTOMATICO", outputData.getPreenchimento());
        assertEquals("EM_ELABORACAO", outputData.getSituacao());
        assertEquals(Long.valueOf(51), outputData.getQuantidadeReservada());
        assertEquals(Long.valueOf(51), outputData.getQuantidadeRestante());
        assertEquals(Long.valueOf(52), outputData.getNumeroInicio());
        assertEquals(Long.valueOf(102), outputData.getNumeroFim());

        verify(reservaDataProvider).bloquearEntidade();
        verify(patrimonioDataProvider).bloquearEntidade();
    }

    @Test(expected = IntervaloNaoInformadoException.class)
    public void deveFalharQuandoFaltaNumeroInicioPreenchimentoManual() {
        EditarReservaPatrimonialInputData inputData = EditarReservaPatrimonialInputData
            .builder()
            .id(2L)
            .preenchimento("MANUAL")
            .build();

        Reserva reserva = Reserva
            .builder()
            .id(2L)
            .codigo("00002")
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .preenchimento(Reserva.Preenchimento.MANUAL)
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .quantidadeReservada(50L)
            .quantidadeRestante(50L)
            .numeroInicio(1L)
            .numeroFim(1L)
            .build();

        when(sessaoUsuarioDataProvider.get())
            .thenReturn(SessaoUsuario.builder()
                .id(1L)
                .permissoes(List.of("Mobiliario.Normal"))
                .build()
            );

        when(reservaDataProvider.buscarPorId(anyLong(), any(Reserva.Filtro.class)))
            .thenReturn(Optional.of(reserva));

        when(reservaIntervaloDataProvider.existeIntervalosPorReserva(any(Reserva.class)))
            .thenReturn(false);

        useCase.executar(inputData);
    }

    @Test(expected = IntervaloNaoInformadoException.class)
    public void deveFalharQuandoFaltaNumeroFimPreenchimentoManual() {
        EditarReservaPatrimonialInputData inputData = EditarReservaPatrimonialInputData
            .builder()
            .id(2L)
            .preenchimento("MANUAL")
            .numeroInicio(50L)
            .build();

        Reserva reserva = Reserva
            .builder()
            .id(2L)
            .codigo("00002")
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .preenchimento(Reserva.Preenchimento.MANUAL)
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .quantidadeReservada(50L)
            .quantidadeRestante(50L)
            .numeroInicio(1L)
            .numeroFim(1L)
            .build();

        when(sessaoUsuarioDataProvider.get())
            .thenReturn(SessaoUsuario.builder()
                .id(1L)
                .permissoes(List.of("Mobiliario.Normal"))
                .build()
            );

        when(reservaDataProvider.buscarPorId(anyLong(), any(Reserva.Filtro.class)))
            .thenReturn(Optional.of(reserva));

        when(reservaIntervaloDataProvider.existeIntervalosPorReserva(any(Reserva.class)))
            .thenReturn(false);

        useCase.executar(inputData);
    }

    @Test(expected = IntervaloInvalidoException.class)
    public void deveFalharQuandoIntervaloInvalido() {
        EditarReservaPatrimonialInputData inputData = EditarReservaPatrimonialInputData
            .builder()
            .id(2L)
            .preenchimento("MANUAL")
            .numeroInicio(50L)
            .numeroFim(49L)
            .build();

        Reserva reserva = Reserva
            .builder()
            .id(2L)
            .codigo("00002")
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .preenchimento(Reserva.Preenchimento.MANUAL)
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .quantidadeReservada(50L)
            .quantidadeRestante(50L)
            .numeroInicio(1L)
            .numeroFim(1L)
            .build();

        when(sessaoUsuarioDataProvider.get())
            .thenReturn(SessaoUsuario.builder()
                .id(1L)
                .permissoes(List.of("Mobiliario.Normal"))
                .build()
            );

        when(reservaDataProvider.buscarPorId(anyLong(), any(Reserva.Filtro.class)))
            .thenReturn(Optional.of(reserva));

        when(reservaIntervaloDataProvider.existeIntervalosPorReserva(any(Reserva.class)))
            .thenReturn(false);

        useCase.executar(inputData);
    }

    @Test
    public void deveRetornarReservaQuandoNenhumCampoAlteradoPreenchimentoManual() {
        EditarReservaPatrimonialInputData inputData = EditarReservaPatrimonialInputData
            .builder()
            .id(2L)
            .preenchimento("MANUAL")
            .numeroInicio(50L)
            .numeroFim(100L)
            .build();

        Reserva reserva = Reserva
            .builder()
            .id(2L)
            .codigo("00002")
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .preenchimento(Reserva.Preenchimento.MANUAL)
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .quantidadeReservada(51L)
            .quantidadeRestante(51L)
            .numeroInicio(50L)
            .numeroFim(100L)
            .build();

        when(sessaoUsuarioDataProvider.get())
            .thenReturn(SessaoUsuario.builder()
                .id(1L)
                .permissoes(List.of("Mobiliario.Normal"))
                .build()
            );

        when(reservaDataProvider.buscarPorId(anyLong(), any(Reserva.Filtro.class)))
            .thenReturn(Optional.of(reserva));

        when(reservaIntervaloDataProvider.existeIntervalosPorReserva(any(Reserva.class)))
            .thenReturn(false);

        EditarReservaPatrimonialOutputData outputData = useCase.executar(inputData);

        assertEquals(Long.valueOf(2), outputData.getId());
        assertEquals("00002", outputData.getCodigo());
        assertEquals("MANUAL", outputData.getPreenchimento());
        assertEquals("EM_ELABORACAO", outputData.getSituacao());
        assertEquals(Long.valueOf(51), outputData.getQuantidadeReservada());
        assertEquals(Long.valueOf(51), outputData.getQuantidadeRestante());
        assertEquals(Long.valueOf(50), outputData.getNumeroInicio());
        assertEquals(Long.valueOf(100), outputData.getNumeroFim());

        verify(reservaDataProvider).bloquearEntidade();
        verify(patrimonioDataProvider).bloquearEntidade();
    }

    @Test(expected = IntervaloEmUsoException.class)
    public void deveFalharQuandoIntervaloEmUso() {
        EditarReservaPatrimonialInputData inputData = EditarReservaPatrimonialInputData
            .builder()
            .id(2L)
            .preenchimento("MANUAL")
            .numeroInicio(50L)
            .numeroFim(100L)
            .build();

        Reserva reserva = Reserva
            .builder()
            .id(2L)
            .codigo("00002")
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .preenchimento(Reserva.Preenchimento.MANUAL)
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .quantidadeReservada(50L)
            .quantidadeRestante(50L)
            .numeroInicio(50L)
            .numeroFim(99L)
            .build();

        when(sessaoUsuarioDataProvider.get())
            .thenReturn(SessaoUsuario.builder()
                .id(1L)
                .permissoes(List.of("Mobiliario.Normal"))
                .build()
            );

        when(reservaDataProvider.buscarPorId(anyLong(), any(Reserva.Filtro.class)))
            .thenReturn(Optional.of(reserva));

        when(reservaIntervaloDataProvider.existeIntervalosPorReserva(any(Reserva.class)))
            .thenReturn(false);

        when(reservaDataProvider.existeEmIntervalo(any(Reserva.Filtro.class)))
            .thenReturn(true);

        useCase.executar(inputData);
    }

    @Test(expected = IntervaloEmUsoException.class)
    public void deveFalharQuandoIntervaloEmUsoPorPatrimonio() {
        EditarReservaPatrimonialInputData inputData = EditarReservaPatrimonialInputData
            .builder()
            .id(2L)
            .preenchimento("MANUAL")
            .numeroInicio(50L)
            .numeroFim(100L)
            .build();

        Reserva reserva = Reserva
            .builder()
            .id(2L)
            .codigo("00002")
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .preenchimento(Reserva.Preenchimento.MANUAL)
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .quantidadeReservada(50L)
            .quantidadeRestante(50L)
            .numeroInicio(50L)
            .numeroFim(99L)
            .build();

        when(sessaoUsuarioDataProvider.get())
            .thenReturn(SessaoUsuario.builder()
                .id(1L)
                .permissoes(List.of("Mobiliario.Normal"))
                .build()
            );

        when(reservaDataProvider.buscarPorId(anyLong(), any(Reserva.Filtro.class)))
            .thenReturn(Optional.of(reserva));

        when(reservaIntervaloDataProvider.existeIntervalosPorReserva(any(Reserva.class)))
            .thenReturn(false);

        when(reservaDataProvider.existeEmIntervalo(any(Reserva.Filtro.class)))
            .thenReturn(false);

        when(patrimonioDataProvider.existeEmIntervalo(any(Patrimonio.Filtro.class)))
            .thenReturn(true);

        useCase.executar(inputData);
    }

    @Test
    public void deveEditarReservaPreenchimentoManual() {
        EditarReservaPatrimonialInputData inputData = EditarReservaPatrimonialInputData
            .builder()
            .id(2L)
            .preenchimento("MANUAL")
            .numeroInicio(50L)
            .numeroFim(100L)
            .build();

        Reserva reserva = Reserva
            .builder()
            .id(2L)
            .codigo("00002")
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .preenchimento(Reserva.Preenchimento.MANUAL)
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .quantidadeReservada(50L)
            .quantidadeRestante(50L)
            .numeroInicio(50L)
            .numeroFim(99L)
            .build();

        Reserva reservaAlterada = Reserva
            .builder()
            .id(2L)
            .codigo("00002")
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .preenchimento(Reserva.Preenchimento.MANUAL)
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .quantidadeReservada(51L)
            .quantidadeRestante(51L)
            .numeroInicio(50L)
            .numeroFim(100L)
            .build();

        when(sessaoUsuarioDataProvider.get())
            .thenReturn(SessaoUsuario.builder()
                .id(1L)
                .permissoes(List.of("Mobiliario.Normal"))
                .build()
            );

        when(reservaDataProvider.buscarPorId(anyLong(), any(Reserva.Filtro.class)))
            .thenReturn(Optional.of(reserva));

        when(reservaIntervaloDataProvider.existeIntervalosPorReserva(any(Reserva.class)))
            .thenReturn(false);

        when(reservaDataProvider.salvar(any(Reserva.class)))
            .thenReturn(reservaAlterada);

        EditarReservaPatrimonialOutputData outputData = useCase.executar(inputData);

        verify(reservaDataProvider, times(1)).salvar(reservaAlterada);
        assertEquals(Long.valueOf(2), outputData.getId());
        assertEquals("00002", outputData.getCodigo());
        assertEquals("MANUAL", outputData.getPreenchimento());
        assertEquals("EM_ELABORACAO", outputData.getSituacao());
        assertEquals(Long.valueOf(51), outputData.getQuantidadeReservada());
        assertEquals(Long.valueOf(51), outputData.getQuantidadeRestante());
        assertEquals(Long.valueOf(50), outputData.getNumeroInicio());
        assertEquals(Long.valueOf(100), outputData.getNumeroFim());

        verify(reservaDataProvider).bloquearEntidade();
        verify(patrimonioDataProvider).bloquearEntidade();
    }
}
