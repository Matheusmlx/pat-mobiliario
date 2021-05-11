package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao;

import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervaloNumero;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloNumeroDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.inserir.InserirReservaPorOrgaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.inserir.InserirReservaPorOrgaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.inserir.InserirReservaPorOrgaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.inserir.converter.InserirReservaPorOrgaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.inserir.exception.IntervaloEmUsoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.inserir.exception.IntervaloInvalidoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.inserir.exception.IntervaloNaoInformadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.inserir.exception.PreenchimentoInvalidoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.inserir.exception.QuantidadeReservadaException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InserirReservaPorOrgaoUseCaseTest {

    private static final LocalDate LOCAL_DATE = LocalDate.of(2021, 8, 12);

    @Mock
    private ReservaDataProvider reservaDataProvider;

    @Mock
    private ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    @Mock
    private ReservaIntervaloNumeroDataProvider reservaIntervaloNumeroDataProvider;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @InjectMocks
    private InserirReservaPorOrgaoOutputDataConverter outputDataConverter;

    private final Clock clock = Clock.fixed(LOCAL_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    private InserirReservaPorOrgaoUseCase useCase;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        useCase = new InserirReservaPorOrgaoUseCaseImpl(
            clock,
            reservaDataProvider,
            reservaIntervaloDataProvider,
            reservaIntervaloNumeroDataProvider,
            patrimonioDataProvider,
            outputDataConverter
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoTipoPreenchimentoNulo() {
        InserirReservaPorOrgaoInputData inputData = InserirReservaPorOrgaoInputData
            .builder()
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoOrgaoIdNulo() {
        InserirReservaPorOrgaoInputData inputData = InserirReservaPorOrgaoInputData
            .builder()
            .preenchimento("AUTOMATICO")
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = PreenchimentoInvalidoException.class)
    public void deveFalharQuandoPreenchimentoInvalido() {
        InserirReservaPorOrgaoInputData inputData = InserirReservaPorOrgaoInputData
            .builder()
            .preenchimento("AUTOMATIC")
            .orgaoId(12L)
            .build();

        useCase.executar(inputData);
    }

    @Test
    public void deveFalharQuandoQuantidadeNaoInformadaPreenchimentoAutomatico() {
        InserirReservaPorOrgaoInputData inputData = InserirReservaPorOrgaoInputData
            .builder()
            .preenchimento("AUTOMATICO")
            .orgaoId(12L)
            .build();

        exception.expect(QuantidadeReservadaException.class);
        exception.expectMessage("A quantidade a ser reservada não foi informada");

        useCase.executar(inputData);
    }

    @Test
    public void deveFalharQuandoQuantidadeMenorIgualZeroPreenchimentoAutomatico() {
        InserirReservaPorOrgaoInputData inputData = InserirReservaPorOrgaoInputData
            .builder()
            .preenchimento("AUTOMATICO")
            .orgaoId(12L)
            .quantidadeReservada(-1L)
            .build();

        exception.expect(QuantidadeReservadaException.class);
        exception.expectMessage("A quantidade a ser reservada deve ser maior que 0");

        useCase.executar(inputData);
    }

    @Test(expected = IntervaloEmUsoException.class)
    public void deveFalharQuandoIntervaloEmUsoPreenchimentoAutomatico() {
        InserirReservaPorOrgaoInputData inputData = InserirReservaPorOrgaoInputData
            .builder()
            .preenchimento("AUTOMATICO")
            .orgaoId(12L)
            .quantidadeReservada(15L)
            .build();

        when(reservaDataProvider.existeEmIntervaloPorOrgao(any(Reserva.Filtro.class)))
            .thenReturn(true);

        useCase.executar(inputData);
    }

    @Test
    public void deveCadastrarNovaReservaPorOrgaoPreenchimentoAutomatico() {
        InserirReservaPorOrgaoInputData inputData = InserirReservaPorOrgaoInputData
            .builder()
            .preenchimento("AUTOMATICO")
            .orgaoId(12L)
            .quantidadeReservada(15L)
            .build();

        Reserva reserva = Reserva
            .builder()
            .codigo("00002")
            .preenchimento(Reserva.Preenchimento.AUTOMATICO)
            .situacao(Reserva.Situacao.FINALIZADO)
            .quantidadeReservada(15L)
            .quantidadeRestante(15L)
            .dataCriacao(LocalDateTime.now(clock))
            .numeroInicio(1L)
            .numeroFim(15L)
            .build();

        Reserva reservaSalva = Reserva
            .builder()
            .id(2L)
            .codigo("00002")
            .situacao(Reserva.Situacao.FINALIZADO)
            .preenchimento(Reserva.Preenchimento.AUTOMATICO)
            .dataCriacao(LocalDateTime.now(clock))
            .quantidadeReservada(15L)
            .quantidadeRestante(15L)
            .numeroInicio(1L)
            .numeroFim(15L)
            .build();

        ReservaIntervalo intervalo = ReservaIntervalo
            .builder()
            .codigo("00001")
            .reserva(reservaSalva)
            .orgao(UnidadeOrganizacional.builder().id(inputData.getOrgaoId()).build())
            .situacao(ReservaIntervalo.Situacao.FINALIZADO)
            .preenchimento(ReservaIntervalo.Preenchimento.valueOf(
                reservaSalva.getPreenchimento().name()
            ))
            .quantidadeReservada(reservaSalva.getQuantidadeReservada())
            .numeroInicio(reservaSalva.getNumeroInicio())
            .numeroFim(reservaSalva.getNumeroFim())
            .dataFinalizacao(LocalDateTime.now(clock))
            .build();

        ReservaIntervalo intervaloSalvo = ReservaIntervalo
            .builder()
            .id(1L)
            .codigo("00001")
            .reserva(reservaSalva)
            .orgao(UnidadeOrganizacional.builder().id(inputData.getOrgaoId()).build())
            .situacao(ReservaIntervalo.Situacao.FINALIZADO)
            .preenchimento(ReservaIntervalo.Preenchimento.valueOf(
                reservaSalva.getPreenchimento().name()
            ))
            .quantidadeReservada(reservaSalva.getQuantidadeReservada())
            .numeroInicio(reservaSalva.getNumeroInicio())
            .numeroFim(reservaSalva.getNumeroFim())
            .dataFinalizacao(LocalDateTime.now(clock))
            .build();

        List<ReservaIntervaloNumero> numerosIntervalo = preencherListaReservaNumeros(intervaloSalvo);

        when(reservaDataProvider.buscarReservaComMaiorNumeroFimParaOrgao(anyLong()))
            .thenReturn(Optional.empty());

        when(patrimonioDataProvider.buscarPatrimonioComMaiorNumeroPorOrgao(anyLong()))
            .thenReturn(Optional.empty());

        when(reservaDataProvider.buscarReservaComMaiorCodigoPorOrgao(anyLong()))
            .thenReturn(
                Optional.of(
                    Reserva
                        .builder()
                        .codigo("00001")
                        .build()
                )
            );

        when(reservaDataProvider.salvar(any(Reserva.class)))
            .thenReturn(reservaSalva);

        when(reservaIntervaloDataProvider.buscarIntervaloComMaiorCodigoPorOrgao(anyLong()))
            .thenReturn(Optional.empty());

        when(reservaIntervaloDataProvider.salvar(any(ReservaIntervalo.class)))
            .thenReturn(intervaloSalvo);

        useCase.executar(inputData);

        verify(reservaDataProvider, times(1)).salvar(reserva);
        verify(reservaIntervaloDataProvider, times(1)).salvar(intervalo);
        verify(reservaIntervaloNumeroDataProvider, times(1)).salvar(numerosIntervalo);
        verify(reservaDataProvider).bloquearEntidade();
        verify(patrimonioDataProvider).bloquearEntidade();
    }

    @Test(expected = IntervaloNaoInformadoException.class)
    public void deveFalharQuandoIntervaloNaoInformadoPreenchimentoManual() {
        InserirReservaPorOrgaoInputData inputData = InserirReservaPorOrgaoInputData
            .builder()
            .preenchimento("MANUAL")
            .orgaoId(12L)
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = IntervaloInvalidoException.class)
    public void deveFalharQuandoIntervaloInvalidoPreenchimentoManual() {
        InserirReservaPorOrgaoInputData inputData = InserirReservaPorOrgaoInputData
            .builder()
            .preenchimento("MANUAL")
            .orgaoId(12L)
            .numeroInicio(15L)
            .numeroFim(1L)
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = IntervaloEmUsoException.class)
    public void deveFalharQuandoIntervaloUtilizadoReservaPreenchimentoManual() {
        InserirReservaPorOrgaoInputData inputData = InserirReservaPorOrgaoInputData
            .builder()
            .preenchimento("MANUAL")
            .orgaoId(12L)
            .numeroInicio(1L)
            .numeroFim(15L)
            .build();

        when(reservaDataProvider.existeEmIntervaloPorOrgao(any(Reserva.Filtro.class)))
            .thenReturn(true);

        useCase.executar(inputData);
    }

    @Test(expected = IntervaloEmUsoException.class)
    public void deveFalharQuandoIntervaloUtilizadoPatrimonioPreenchimentoManual() {
        InserirReservaPorOrgaoInputData inputData = InserirReservaPorOrgaoInputData
            .builder()
            .preenchimento("MANUAL")
            .orgaoId(12L)
            .numeroInicio(1L)
            .numeroFim(15L)
            .build();

        when(reservaDataProvider.existeEmIntervaloPorOrgao(any(Reserva.Filtro.class)))
            .thenReturn(false);

        when(patrimonioDataProvider.existeEmIntervalo(any(Patrimonio.Filtro.class)))
            .thenReturn(true);

        useCase.executar(inputData);
    }

    @Test
    public void deveCadastrarNovaReservaPorOrgaoPreenchimentoManual() {
        InserirReservaPorOrgaoInputData inputData = InserirReservaPorOrgaoInputData
            .builder()
            .preenchimento("MANUAL")
            .orgaoId(12L)
            .numeroInicio(1L)
            .numeroFim(15L)
            .build();

        Reserva reserva = Reserva
            .builder()
            .codigo("00001")
            .preenchimento(Reserva.Preenchimento.MANUAL)
            .situacao(Reserva.Situacao.FINALIZADO)
            .dataCriacao(LocalDateTime.now(clock))
            .numeroInicio(1L)
            .numeroFim(15L)
            .quantidadeReservada(15L)
            .quantidadeRestante(15L)
            .build();

        Reserva reservaSalva = Reserva
            .builder()
            .id(1L)
            .codigo("00001")
            .preenchimento(Reserva.Preenchimento.MANUAL)
            .situacao(Reserva.Situacao.FINALIZADO)
            .dataCriacao(LocalDateTime.now(clock))
            .numeroInicio(1L)
            .numeroFim(15L)
            .quantidadeReservada(15L)
            .quantidadeRestante(15L)
            .build();

        ReservaIntervalo reservaIntervalo = ReservaIntervalo
            .builder()
            .codigo("00001")
            .reserva(reservaSalva)
            .orgao(UnidadeOrganizacional.builder().id(inputData.getOrgaoId()).build())
            .situacao(ReservaIntervalo.Situacao.FINALIZADO)
            .preenchimento(ReservaIntervalo.Preenchimento.valueOf(
                reservaSalva.getPreenchimento().name()
            ))
            .quantidadeReservada(reservaSalva.getQuantidadeReservada())
            .numeroInicio(reservaSalva.getNumeroInicio())
            .numeroFim(reservaSalva.getNumeroFim())
            .dataFinalizacao(LocalDateTime.now(clock))
            .build();

        ReservaIntervalo intervaloSalvo = ReservaIntervalo
            .builder()
            .id(1L)
            .codigo("00001")
            .reserva(reservaSalva)
            .orgao(UnidadeOrganizacional.builder().id(inputData.getOrgaoId()).build())
            .situacao(ReservaIntervalo.Situacao.FINALIZADO)
            .preenchimento(ReservaIntervalo.Preenchimento.valueOf(
                reservaSalva.getPreenchimento().name()
            ))
            .quantidadeReservada(reservaSalva.getQuantidadeReservada())
            .numeroInicio(reservaSalva.getNumeroInicio())
            .numeroFim(reservaSalva.getNumeroFim())
            .dataFinalizacao(LocalDateTime.now(clock))
            .build();

        List<ReservaIntervaloNumero> numerosIntervalo = preencherListaReservaNumeros(intervaloSalvo);

        when(reservaDataProvider.existeEmIntervaloPorOrgao(any(Reserva.Filtro.class)))
            .thenReturn(false);

        when(patrimonioDataProvider.existeEmIntervalo(any(Patrimonio.Filtro.class)))
            .thenReturn(false);

        when(reservaDataProvider.buscarReservaComMaiorCodigoPorOrgao(anyLong()))
            .thenReturn(Optional.empty());

        when(reservaDataProvider.salvar(any(Reserva.class)))
            .thenReturn(reservaSalva);

        when(reservaIntervaloDataProvider.buscarIntervaloComMaiorCodigoPorOrgao(anyLong()))
            .thenReturn(Optional.empty());

        when(reservaIntervaloDataProvider.salvar(any(ReservaIntervalo.class)))
            .thenReturn(intervaloSalvo);

        useCase.executar(inputData);

        verify(reservaDataProvider, times(1)).salvar(reserva);
        verify(reservaIntervaloDataProvider, times(1)).salvar(reservaIntervalo);
        verify(reservaIntervaloNumeroDataProvider, times(1)).salvar(numerosIntervalo);
        verify(reservaDataProvider).bloquearEntidade();
        verify(patrimonioDataProvider).bloquearEntidade();
    }


    private List<ReservaIntervaloNumero> preencherListaReservaNumeros(ReservaIntervalo intervalo) {
        List<ReservaIntervaloNumero> numerosIntervalo = new ArrayList<>();

        for (Long numero = intervalo.getNumeroInicio(); numero <= intervalo.getNumeroFim(); numero++) {
            numerosIntervalo.add(
                ReservaIntervaloNumero
                    .builder()
                    .numero(numero)
                    .reservaIntervalo(intervalo)
                    .utilizado(false)
                    .build()
            );
        }

        return numerosIntervalo;
    }


}
