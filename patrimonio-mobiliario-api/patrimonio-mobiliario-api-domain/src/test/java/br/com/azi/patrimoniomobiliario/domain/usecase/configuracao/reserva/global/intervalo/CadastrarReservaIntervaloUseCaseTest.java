package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo;


import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.entity.SessaoUsuario;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.CadastrarReservaIntervaloInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.CadastrarReservaIntervaloUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.CadastrarReservaIntervaloUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.exception.CampoNaoInformadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.exception.InterseccaoEntreIntervalosException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.exception.IntervaloComNumeroFimPosteriorAReservaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.exception.IntervaloComNumeroInicioAnteriorAReservaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.exception.IntervaloComQuantidadeIncorretaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.exception.IntervaloEmUsoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.exception.IntervaloInvalidoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.exception.IntervaloNaoInformadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.exception.QuantidadeTotalDeNumerosDosIntervalosMaiorQueAQuantidadeRestanteException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CadastrarReservaIntervaloUseCaseTest {

    @Mock
    private ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    @Mock
    private ReservaDataProvider reservaDataProvider;

    @Mock
    private PatrimonioDataProvider patrimonioDataProvider;

    @Mock
    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    private CadastrarReservaIntervaloUseCase useCase;

    @Before
    public void setUp() {
        useCase = new CadastrarReservaIntervaloUseCaseImpl(
                reservaIntervaloDataProvider,
                reservaDataProvider,
                patrimonioDataProvider,
                sessaoUsuarioDataProvider
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoReservaIdNaoInformado() {
        CadastrarReservaIntervaloInputData inputData = new CadastrarReservaIntervaloInputData();

        useCase.executar(inputData);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoListaDeIntervalosDaReservaNaoInformado() {
        CadastrarReservaIntervaloInputData inputData = new CadastrarReservaIntervaloInputData();
        inputData.setReservaId(1L);

        useCase.executar(inputData);
    }

    @Test(expected = CampoNaoInformadoException.class)
    public void deveFalharQuandoAlgumCampoPreenchimentoDaListaDeIntervalosDaReservaNaoInformado() {
        CadastrarReservaIntervaloInputData inputData = new CadastrarReservaIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setIntervalos(
                List.of(
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(100L)
                                .numeroInicio(200L)
                                .numeroFim(300L)
                                .build())
        );

        useCase.executar(inputData);
    }

    @Test(expected = CampoNaoInformadoException.class)
    public void deveFalharQuandoAlgumCampoOrgaoDaListaDeIntervalosDaReservaNaoInformado() {
        CadastrarReservaIntervaloInputData inputData = new CadastrarReservaIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setIntervalos(
                List.of(
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .preenchimento("MANUAL")
                                .quantidadeReservada(100L)
                                .numeroInicio(200L)
                                .numeroFim(300L)
                                .build())
        );

        useCase.executar(inputData);
    }

    @Test(expected = CampoNaoInformadoException.class)
    public void deveFalharQuandoAlgumCampoQuantidadeDaListaDeIntervalosDaReservaNaoInformado() {
        CadastrarReservaIntervaloInputData inputData = new CadastrarReservaIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setIntervalos(
                List.of(
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .preenchimento("MANUAL")
                                .numeroInicio(200L)
                                .numeroFim(300L)
                                .build())
        );

        useCase.executar(inputData);
    }

    @Test(expected = CampoNaoInformadoException.class)
    public void deveFalharQuandoAlgumCampoQuantidadeDaListaDeIntervalosDaReservaZero() {
        CadastrarReservaIntervaloInputData inputData = new CadastrarReservaIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setIntervalos(
                List.of(
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(0L)
                                .preenchimento("MANUAL")
                                .numeroInicio(0L)
                                .numeroFim(0L)
                                .build())
        );

        useCase.executar(inputData);
    }

    @Test(expected = CampoNaoInformadoException.class)
    public void deveFalharQuandoAlgumCampoQuantidadeDaListaDeIntervalosDaReservaMenorQueZero() {
        CadastrarReservaIntervaloInputData inputData = new CadastrarReservaIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setIntervalos(
                List.of(
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(-1L)
                                .preenchimento("MANUAL")
                                .numeroInicio(0L)
                                .numeroFim(0L)
                                .build())
        );

        useCase.executar(inputData);
    }

    @Test(expected = IntervaloNaoInformadoException.class)
    public void deveFalharQuandoAlgumCampoNumeroInicioDaListaDeIntervalosDaReservaNaoInformado() {
        CadastrarReservaIntervaloInputData inputData = new CadastrarReservaIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setIntervalos(
                List.of(
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(100L)
                                .preenchimento("MANUAL")
                                .numeroFim(300L)
                                .build())
        );

        useCase.executar(inputData);
    }

    @Test(expected = IntervaloNaoInformadoException.class)
    public void deveFalharQuandoAlgumCampoNumeroFimDaListaDeIntervalosDaReservaNaoInformado() {
        CadastrarReservaIntervaloInputData inputData = new CadastrarReservaIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setIntervalos(
                List.of(
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(100L)
                                .preenchimento("MANUAL")
                                .numeroInicio(200L)
                                .build())
        );

        useCase.executar(inputData);
    }

    @Test(expected = IntervaloInvalidoException.class)
    public void deveFalharQuandoAlgumCampoNumeroInicioDaListaDeIntervalosDaReservaZero() {
        CadastrarReservaIntervaloInputData inputData = new CadastrarReservaIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setIntervalos(
                List.of(
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(100L)
                                .preenchimento("MANUAL")
                                .numeroInicio(0L)
                                .numeroFim(300L)
                                .build())
        );

        useCase.executar(inputData);
    }

    @Test(expected = IntervaloInvalidoException.class)
    public void deveFalharQuandoAlgumCampoNumeroInicioDaListaDeIntervalosDaReservaMenorQueZero() {
        CadastrarReservaIntervaloInputData inputData = new CadastrarReservaIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setIntervalos(
                List.of(
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(100L)
                                .preenchimento("MANUAL")
                                .numeroInicio(-1L)
                                .numeroFim(300L)
                                .build())
        );

        useCase.executar(inputData);
    }

    @Test(expected = IntervaloInvalidoException.class)
    public void deveFalharQuandoAlgumCampoNumeroInicioDaListaDeIntervalosDaReservaMaiorQueNumeroFim() {
        CadastrarReservaIntervaloInputData inputData = new CadastrarReservaIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setIntervalos(
                List.of(
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(100L)
                                .preenchimento("MANUAL")
                                .numeroInicio(400L)
                                .numeroFim(300L)
                                .build())
        );

        useCase.executar(inputData);
    }

    @Test(expected = IntervaloComQuantidadeIncorretaException.class)
    public void deveFalharQuandoAlgumCampoQuantidadeDaListaDeIntervalosDaReservaMaiorQueQuantidadeDeNumerosNoIntervalo() {
        CadastrarReservaIntervaloInputData inputData = new CadastrarReservaIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setIntervalos(
                List.of(
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(102L)
                                .preenchimento("MANUAL")
                                .numeroInicio(300L)
                                .numeroFim(400L)
                                .build())
        );

        useCase.executar(inputData);
    }

    @Test(expected = IntervaloComQuantidadeIncorretaException.class)
    public void deveFalharQuandoAlgumCampoQuantidadeDaListaDeIntervalosDaReservaMenorQueQuantidadeDeNumerosNoIntervalo() {
        CadastrarReservaIntervaloInputData inputData = new CadastrarReservaIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setIntervalos(
                List.of(
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(99L)
                                .preenchimento("MANUAL")
                                .numeroInicio(300L)
                                .numeroFim(400L)
                                .build())
        );

        useCase.executar(inputData);
    }

    @Test(expected = IntervaloComNumeroInicioAnteriorAReservaException.class)
    public void deveFalharQuandoMenorNumeroInicioDoIntervaloDaReservaForMenorQueNumeroInicioDaReserva() {
        CadastrarReservaIntervaloInputData inputData = new CadastrarReservaIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setIntervalos(
                List.of(
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(101L)
                                .preenchimento("MANUAL")
                                .numeroInicio(300L)
                                .numeroFim(400L)
                                .build())
        );

        when(sessaoUsuarioDataProvider.get())
                .thenReturn(SessaoUsuario.builder()
                        .id(1L)
                        .permissoes(List.of("Mobiliario.Normal"))
                        .build()
                );

        when(reservaDataProvider.buscarPorId(any(Long.class), any(Reserva.Filtro.class)))
                .thenReturn(Optional.of(Reserva
                        .builder()
                        .id(1L)
                        .numeroInicio(301L)
                        .build()));

        useCase.executar(inputData);
    }

    @Test(expected = IntervaloComNumeroFimPosteriorAReservaException.class)
    public void deveFalharQuandoMaiorNumeroFimDoIntervaloDaReservaForMaiorQueNumeroFimDaReserva() {
        CadastrarReservaIntervaloInputData inputData = new CadastrarReservaIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setIntervalos(
                List.of(
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(101L)
                                .preenchimento("MANUAL")
                                .numeroInicio(300L)
                                .numeroFim(400L)
                                .build())
        );

        when(sessaoUsuarioDataProvider.get())
                .thenReturn(SessaoUsuario.builder()
                        .id(1L)
                        .permissoes(List.of("Mobiliario.Normal"))
                        .build()
                );

        when(reservaDataProvider.buscarPorId(any(Long.class), any(Reserva.Filtro.class)))
                .thenReturn(Optional.of(Reserva
                        .builder()
                        .id(1L)
                        .numeroInicio(300L)
                        .numeroFim(399L)
                        .build()));

        useCase.executar(inputData);
    }

    @Test(expected = QuantidadeTotalDeNumerosDosIntervalosMaiorQueAQuantidadeRestanteException.class)
    public void deveFalharSeTotalDasQuantidadesDosIntervalosDaReservaForMaiorQueQuantidadeRestanteDaReserva() {
        CadastrarReservaIntervaloInputData inputData = new CadastrarReservaIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setIntervalos(
                List.of(
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(100L)
                                .preenchimento("MANUAL")
                                .numeroInicio(300L)
                                .numeroFim(399L)
                                .build(),
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(100L)
                                .preenchimento("AUTOMATICO")
                                .numeroInicio(401L)
                                .numeroFim(500L)
                                .build())
        );

        when(sessaoUsuarioDataProvider.get())
                .thenReturn(SessaoUsuario.builder()
                        .id(1L)
                        .permissoes(List.of("Mobiliario.Normal"))
                        .build()
                );

        when(reservaDataProvider.buscarPorId(any(Long.class), any(Reserva.Filtro.class)))
                .thenReturn(Optional.of(Reserva
                        .builder()
                        .id(1L)
                        .numeroInicio(200L)
                        .numeroFim(600L)
                        .quantidadeRestante(100L)
                        .build()));

        useCase.executar(inputData);
    }


    @Test(expected = InterseccaoEntreIntervalosException.class)
    public void deveFalharSeHouverInterseccaoEntreIntervalosDaReserva() {
        CadastrarReservaIntervaloInputData inputData = new CadastrarReservaIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setIntervalos(
                List.of(
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(100L)
                                .preenchimento("MANUAL")
                                .numeroInicio(200L)
                                .numeroFim(299L)
                                .build(),
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(101L)
                                .preenchimento("AUTOMATICO")
                                .numeroInicio(200L)
                                .numeroFim(300L)
                                .build())
        );

        when(sessaoUsuarioDataProvider.get())
                .thenReturn(SessaoUsuario.builder()
                        .id(1L)
                        .permissoes(List.of("Mobiliario.Normal"))
                        .build()
                );

        when(reservaDataProvider.buscarPorId(any(Long.class), any(Reserva.Filtro.class)))
                .thenReturn(Optional.of(Reserva
                        .builder()
                        .id(1L)
                        .numeroInicio(200L)
                        .numeroFim(600L)
                        .quantidadeRestante(201L)
                        .build()));

        useCase.executar(inputData);
    }

    @Test(expected = InterseccaoEntreIntervalosException.class)
    public void deveFalharSeHouverInterseccaoEntreIntervalosDaReserva1() {
        CadastrarReservaIntervaloInputData inputData = new CadastrarReservaIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setIntervalos(
                List.of(
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(100L)
                                .preenchimento("MANUAL")
                                .numeroInicio(200L)
                                .numeroFim(299L)
                                .build(),
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(100L)
                                .preenchimento("AUTOMATICO")
                                .numeroInicio(199L)
                                .numeroFim(298L)
                                .build())
        );

        when(sessaoUsuarioDataProvider.get())
                .thenReturn(SessaoUsuario.builder()
                        .id(1L)
                        .permissoes(List.of("Mobiliario.Normal"))
                        .build()
                );

        when(reservaDataProvider.buscarPorId(any(Long.class), any(Reserva.Filtro.class)))
                .thenReturn(Optional.of(Reserva
                        .builder()
                        .id(1L)
                        .numeroInicio(199L)
                        .numeroFim(600L)
                        .quantidadeRestante(200L)
                        .build()));

        useCase.executar(inputData);
    }

    @Test(expected = InterseccaoEntreIntervalosException.class)
    public void deveFalharSeHouverInterseccaoEntreIntervalosDaReserva2() {
        CadastrarReservaIntervaloInputData inputData = new CadastrarReservaIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setIntervalos(
                List.of(
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(100L)
                                .preenchimento("MANUAL")
                                .numeroInicio(200L)
                                .numeroFim(299L)
                                .build(),
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(102L)
                                .preenchimento("AUTOMATICO")
                                .numeroInicio(199L)
                                .numeroFim(300L)
                                .build())
        );

        when(sessaoUsuarioDataProvider.get())
                .thenReturn(SessaoUsuario.builder()
                        .id(1L)
                        .permissoes(List.of("Mobiliario.Normal"))
                        .build()
                );

        when(reservaDataProvider.buscarPorId(any(Long.class), any(Reserva.Filtro.class)))
                .thenReturn(Optional.of(Reserva
                        .builder()
                        .id(1L)
                        .numeroInicio(199L)
                        .numeroFim(600L)
                        .quantidadeRestante(202L)
                        .build()));

        useCase.executar(inputData);
    }

    @Test(expected = IntervaloEmUsoException.class)
    public void deveFalharSeIntervaloDaReservaEmUsoEmOutraReserva() {
        CadastrarReservaIntervaloInputData inputData = new CadastrarReservaIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setIntervalos(
                List.of(
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(100L)
                                .preenchimento("MANUAL")
                                .numeroInicio(300L)
                                .numeroFim(399L)
                                .build(),
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(100L)
                                .preenchimento("AUTOMATICO")
                                .numeroInicio(401L)
                                .numeroFim(500L)
                                .build())
        );

        when(sessaoUsuarioDataProvider.get())
                .thenReturn(SessaoUsuario.builder()
                        .id(1L)
                        .permissoes(List.of("Mobiliario.Normal"))
                        .build()
                );

        when(reservaDataProvider.buscarPorId(any(Long.class), any(Reserva.Filtro.class)))
                .thenReturn(Optional.of(Reserva
                        .builder()
                        .id(1L)
                        .numeroInicio(200L)
                        .numeroFim(600L)
                        .quantidadeRestante(200L)
                        .build()));

        when(reservaDataProvider.existeEmIntervaloDeOutraReserva(any(Long.class), any(Long.class), any(Long.class)))
                .thenReturn(true);

        useCase.executar(inputData);
    }

    @Test(expected = IntervaloEmUsoException.class)
    public void deveFalharSeIntervaloDaReservaEmUsoEmOutroPatrimonio() {
        CadastrarReservaIntervaloInputData inputData = new CadastrarReservaIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setIntervalos(
                List.of(
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(100L)
                                .preenchimento("MANUAL")
                                .numeroInicio(300L)
                                .numeroFim(399L)
                                .build(),
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(100L)
                                .preenchimento("AUTOMATICO")
                                .numeroInicio(401L)
                                .numeroFim(500L)
                                .build())
        );

        when(sessaoUsuarioDataProvider.get())
                .thenReturn(SessaoUsuario.builder()
                        .id(1L)
                        .permissoes(List.of("Mobiliario.Normal"))
                        .build()
                );

        when(reservaDataProvider.buscarPorId(any(Long.class), any(Reserva.Filtro.class)))
                .thenReturn(Optional.of(Reserva
                        .builder()
                        .id(1L)
                        .numeroInicio(200L)
                        .numeroFim(600L)
                        .quantidadeRestante(200L)
                        .build()));

        when(reservaDataProvider.existeEmIntervaloDeOutraReserva(any(Long.class), any(Long.class), any(Long.class)))
                .thenReturn(false);

        when(patrimonioDataProvider.existeEmIntervalo(any(Patrimonio.Filtro.class)))
                .thenReturn(true);

        useCase.executar(inputData);
    }

    @Test(expected = IntervaloEmUsoException.class)
    public void deveFalharSeIntervaloDaReservaEmUsoEmNaMesmaReserva() {
        CadastrarReservaIntervaloInputData inputData = new CadastrarReservaIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setIntervalos(
                List.of(
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(100L)
                                .preenchimento("MANUAL")
                                .numeroInicio(300L)
                                .numeroFim(399L)
                                .build(),
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(100L)
                                .preenchimento("AUTOMATICO")
                                .numeroInicio(401L)
                                .numeroFim(500L)
                                .build())
        );

        when(sessaoUsuarioDataProvider.get())
                .thenReturn(SessaoUsuario.builder()
                        .id(1L)
                        .permissoes(List.of("Mobiliario.Normal"))
                        .build()
                );

        when(reservaDataProvider.buscarPorId(any(Long.class), any(Reserva.Filtro.class)))
                .thenReturn(Optional.of(Reserva
                        .builder()
                        .id(1L)
                        .numeroInicio(200L)
                        .numeroFim(600L)
                        .quantidadeRestante(200L)
                        .build()));

        when(reservaDataProvider.existeEmIntervaloDeOutraReserva(any(Long.class), any(Long.class), any(Long.class)))
                .thenReturn(false);

        when(patrimonioDataProvider.existeEmIntervalo(any(Patrimonio.Filtro.class)))
                .thenReturn(false);

        when(reservaIntervaloDataProvider.existeIntervaloNaReserva(any(Long.class), any(Long.class), any(Long.class)))
                .thenReturn(true);

        useCase.executar(inputData);
    }

    @Test
    public void deveSalvarIntervalosDaReservaEAtualizarQuantidadeRestanteDaReserva() {
        CadastrarReservaIntervaloInputData inputData = new CadastrarReservaIntervaloInputData();
        inputData.setReservaId(1L);
        inputData.setIntervalos(
                List.of(
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(1L)
                                .quantidadeReservada(100L)
                                .preenchimento("MANUAL")
                                .numeroInicio(200L)
                                .numeroFim(299L)
                                .build(),
                        CadastrarReservaIntervaloInputData.Intervalo
                                .builder()
                                .orgaoId(2L)
                                .quantidadeReservada(100L)
                                .preenchimento("AUTOMATICO")
                                .numeroInicio(401L)
                                .numeroFim(500L)
                                .build())
        );

        Reserva reserva = Reserva.builder()
            .id(1L)
            .numeroInicio(200L)
            .numeroFim(600L)
            .quantidadeReservada(400L)
            .quantidadeRestante(400L)
            .build();

        Reserva reservaAtualizada = Reserva.builder()
            .id(1L)
            .numeroInicio(200L)
            .numeroFim(600L)
            .quantidadeReservada(400L)
            .quantidadeRestante(200L)
            .build();

        List<ReservaIntervalo> reservaIntervaloParaSalvar = List.of(
            ReservaIntervalo
                .builder()
                .codigo("00001")
                .reserva(reserva)
                .orgao(UnidadeOrganizacional.builder().id(1L).build())
                .situacao(ReservaIntervalo.Situacao.EM_ELABORACAO)
                .preenchimento(ReservaIntervalo.Preenchimento.MANUAL)
                .quantidadeReservada(100L)
                .numeroInicio(200L)
                .numeroFim(299L)
                .build(),
            ReservaIntervalo
                .builder()
                .codigo("00002")
                .reserva(reserva)
                .orgao(UnidadeOrganizacional.builder().id(2L).build())
                .situacao(ReservaIntervalo.Situacao.EM_ELABORACAO)
                .preenchimento(ReservaIntervalo.Preenchimento.AUTOMATICO)
                .quantidadeReservada(100L)
                .numeroInicio(401L)
                .numeroFim(500L)
                .build());

        List<ReservaIntervalo> reservaIntervalosSalvas = List.of(
            ReservaIntervalo
                .builder()
                .id(1L)
                .codigo("00001")
                .reserva(reserva)
                .orgao(UnidadeOrganizacional.builder().id(1L).build())
                .situacao(ReservaIntervalo.Situacao.EM_ELABORACAO)
                .preenchimento(ReservaIntervalo.Preenchimento.MANUAL)
                .quantidadeReservada(100L)
                .numeroInicio(200L)
                .numeroFim(299L)
                .build(),
            ReservaIntervalo
                .builder()
                .id(2L)
                .codigo("00002")
                .reserva(reserva)
                .orgao(UnidadeOrganizacional.builder().id(2L).build())
                .situacao(ReservaIntervalo.Situacao.EM_ELABORACAO)
                .preenchimento(ReservaIntervalo.Preenchimento.AUTOMATICO)
                .quantidadeReservada(100L)
                .numeroInicio(401L)
                .numeroFim(500L)
                .build());

        when(sessaoUsuarioDataProvider.get())
                .thenReturn(SessaoUsuario.builder()
                        .id(1L)
                        .permissoes(List.of("Mobiliario.Normal"))
                        .build()
                );

        when(reservaDataProvider.buscarPorId(any(Long.class), any(Reserva.Filtro.class)))
                .thenReturn(Optional.of(reserva));

        when(reservaDataProvider.existeEmIntervaloDeOutraReserva(any(Long.class), any(Long.class), any(Long.class)))
                .thenReturn(false);

        when(patrimonioDataProvider.existeEmIntervalo(any(Patrimonio.Filtro.class)))
                .thenReturn(false);

        when(reservaIntervaloDataProvider.salvar(any(List.class)))
                .thenReturn(reservaIntervalosSalvas);

        useCase.executar(inputData);

        verify(reservaIntervaloDataProvider, times(1)).salvar(reservaIntervaloParaSalvar);
        verify(reservaIntervaloDataProvider).bloquearEntidade();
        verify(reservaDataProvider, times(1)).salvar(reservaAtualizada);
    }

}
