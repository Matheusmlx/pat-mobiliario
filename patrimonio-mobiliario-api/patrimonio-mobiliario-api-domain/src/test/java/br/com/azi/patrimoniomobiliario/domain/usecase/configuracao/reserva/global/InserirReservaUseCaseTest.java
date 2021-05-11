package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global;

import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir.InserirReservaPatrimonialInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir.InserirReservaPatrimonialUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir.InserirReservaPatrimonialUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir.converter.InserirReservaPatrimonialOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir.exception.IntervaloEmUsoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir.exception.IntervaloInvalidoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir.exception.IntervaloNaoInformadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir.exception.PreenchimentoInvalidoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir.exception.QuantidadeReservadaException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InserirReservaUseCaseTest {

    private static final LocalDate LOCAL_DATE = LocalDate.of(2021, 4, 23);
    private static final Clock FIXED_CLOCK = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    @Mock
    private ReservaDataProvider reservaDataProvider;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    private InserirReservaPatrimonialUseCase useCase;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        useCase = new InserirReservaPatrimonialUseCaseImpl(
            FIXED_CLOCK,
            reservaDataProvider,
            patrimonioDataProvider,
            new InserirReservaPatrimonialOutputDataConverter()
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoTipoPreenchimentoNaoInformado() {
        InserirReservaPatrimonialInputData inputData = new InserirReservaPatrimonialInputData();

        useCase.executar(inputData);
    }

    @Test(expected = PreenchimentoInvalidoException.class)
    public void deveFalharQuandoTipoPreenchimentoInvalido() {
        InserirReservaPatrimonialInputData inputData = InserirReservaPatrimonialInputData
            .builder()
            .preenchimento("asdf")
            .build();

        useCase.executar(inputData);
    }

    @Test
    public void deveFalharQuandoQuantidadeReservaAutomaticaNaoInformada() {
        InserirReservaPatrimonialInputData inputData = InserirReservaPatrimonialInputData
            .builder()
            .preenchimento(Reserva.Preenchimento.AUTOMATICO.name())
            .build();

        exception.expect(QuantidadeReservadaException.class);
        exception.expectMessage("A quantidade a ser reservada não foi informada");

        useCase.executar(inputData);
    }

    @Test
    public void deveFalharQuandoQuantidadeReservaMenorIgualQueZero() {
        InserirReservaPatrimonialInputData inputData = InserirReservaPatrimonialInputData
            .builder()
            .preenchimento(Reserva.Preenchimento.AUTOMATICO.name())
            .quantidadeReservada(0L)
            .build();

        exception.expect(QuantidadeReservadaException.class);
        exception.expectMessage("A quantidade a ser reservada deve ser maior que 0");

        useCase.executar(inputData);
    }

    @Test
    public void deveRealizarReservaAutomatica() {
        InserirReservaPatrimonialInputData inputData = InserirReservaPatrimonialInputData
            .builder()
            .preenchimento(Reserva.Preenchimento.AUTOMATICO.name())
            .quantidadeReservada(10L)
            .build();

        Reserva reservaCadastrada = Reserva
            .builder()
            .codigo("00008")
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .preenchimento(Reserva.Preenchimento.valueOf(inputData.getPreenchimento()))
            .quantidadeReservada(10L)
            .quantidadeRestante(10L)
            .numeroInicio(201L)
            .numeroFim(210L)
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .build();

        Reserva ultimaReservaCadastrada = Reserva
            .builder()
            .id(2L)
            .codigo("00007")
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .preenchimento(Reserva.Preenchimento.AUTOMATICO)
            .quantidadeReservada(50L)
            .quantidadeRestante(50L)
            .numeroInicio(100L)
            .numeroFim(200L)
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .build();

        when(reservaDataProvider.buscarReservaComMaiorNumeroFim())
            .thenReturn(Optional.of(ultimaReservaCadastrada));

        when(reservaDataProvider.buscarReservaComMaiorCodigo())
            .thenReturn(Optional.of(ultimaReservaCadastrada));

        when(patrimonioDataProvider.buscarPatrimonioComMaiorNumero())
            .thenReturn(Optional.of(Patrimonio
                .builder()
                .numero("000020")
                .build()));

        when(reservaDataProvider.salvar(any(Reserva.class)))
            .thenReturn(reservaCadastrada);

        useCase.executar(inputData);

        verify(reservaDataProvider).salvar(reservaCadastrada);
        verify(reservaDataProvider).bloquearEntidade();
        verify(patrimonioDataProvider).bloquearEntidade();
    }

    @Test
    public void deveRealizarReservaAutomaticaComUltimoNumeroPatrimonio() {
        InserirReservaPatrimonialInputData inputData = InserirReservaPatrimonialInputData
            .builder()
            .preenchimento(Reserva.Preenchimento.AUTOMATICO.name())
            .quantidadeReservada(10L)
            .build();

        Reserva reservaCadastrada = Reserva
            .builder()
            .codigo("00001")
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .preenchimento(Reserva.Preenchimento.valueOf(inputData.getPreenchimento()))
            .quantidadeReservada(10L)
            .quantidadeRestante(10L)
            .numeroInicio(21L)
            .numeroFim(30L)
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .build();

        when(reservaDataProvider.buscarReservaComMaiorNumeroFim())
            .thenReturn(Optional.empty());

        when(patrimonioDataProvider.buscarPatrimonioComMaiorNumero())
            .thenReturn(Optional.of(Patrimonio
                .builder()
                .numero("000020")
                .build()));

        when(reservaDataProvider.salvar(any(Reserva.class)))
            .thenReturn(reservaCadastrada);

        useCase.executar(inputData);

        verify(reservaDataProvider).salvar(reservaCadastrada);
        verify(reservaDataProvider).bloquearEntidade();
        verify(patrimonioDataProvider).bloquearEntidade();
    }

    @Test(expected = IntervaloNaoInformadoException.class)
    public void deveFalharQuandoNumeroInicialNaoInformado() {
        InserirReservaPatrimonialInputData inputData = InserirReservaPatrimonialInputData
            .builder()
            .preenchimento(Reserva.Preenchimento.MANUAL.name())
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = IntervaloNaoInformadoException.class)
    public void deveFalharQuandoNumeroFinalNaoInformado() {
        InserirReservaPatrimonialInputData inputData = InserirReservaPatrimonialInputData
            .builder()
            .preenchimento(Reserva.Preenchimento.MANUAL.name())
            .numeroInicio(5L)
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = IntervaloInvalidoException.class)
    public void deveFalharQuandoIntervaloInvalido() {
        InserirReservaPatrimonialInputData inputData = InserirReservaPatrimonialInputData
            .builder()
            .preenchimento(Reserva.Preenchimento.MANUAL.name())
            .numeroInicio(5L)
            .numeroFim(4L)
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = IntervaloEmUsoException.class)
    public void deveFalharQuandoIntervaloEmUsoPelaReserva() {
        InserirReservaPatrimonialInputData inputData = InserirReservaPatrimonialInputData
            .builder()
            .preenchimento(Reserva.Preenchimento.MANUAL.name())
            .numeroInicio(15L)
            .numeroFim(20L)
            .build();

        when(reservaDataProvider.existeEmIntervalo(any(Reserva.Filtro.class)))
            .thenReturn(false);

        when(patrimonioDataProvider.existeEmIntervalo(any(Patrimonio.Filtro.class)))
            .thenReturn(true);

        useCase.executar(inputData);
    }

    @Test(expected = IntervaloEmUsoException.class)
    public void deveFalharQuandoIntervaloEmUsoPeloPatrimonio() {
        InserirReservaPatrimonialInputData inputData = InserirReservaPatrimonialInputData
            .builder()
            .preenchimento(Reserva.Preenchimento.MANUAL.name())
            .numeroInicio(201L)
            .numeroFim(220L)
            .build();

        when(reservaDataProvider.existeEmIntervalo(any(Reserva.Filtro.class)))
            .thenReturn(true);

        useCase.executar(inputData);
    }

    @Test
    public void deveRealizarReservaManual() {
        InserirReservaPatrimonialInputData inputData = InserirReservaPatrimonialInputData
            .builder()
            .preenchimento(Reserva.Preenchimento.MANUAL.name())
            .numeroInicio(201L)
            .numeroFim(220L)
            .build();

        Reserva reservaCadastrada = Reserva
            .builder()
            .codigo("00001")
            .situacao(Reserva.Situacao.EM_ELABORACAO)
            .preenchimento(Reserva.Preenchimento.valueOf(inputData.getPreenchimento()))
            .quantidadeReservada(20L)
            .quantidadeRestante(20L)
            .numeroInicio(201L)
            .numeroFim(220L)
            .dataCriacao(LOCAL_DATE.atStartOfDay())
            .build();

        when(reservaDataProvider.salvar(any(Reserva.class)))
            .thenReturn(reservaCadastrada);

        useCase.executar(inputData);

        verify(reservaDataProvider).salvar(reservaCadastrada);
        verify(reservaDataProvider).bloquearEntidade();
        verify(patrimonioDataProvider).bloquearEntidade();
    }

}
