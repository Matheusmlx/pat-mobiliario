package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.inserir;

import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervaloNumero;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloNumeroDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.inserir.converter.InserirReservaPorOrgaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.inserir.exception.IntervaloEmUsoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.inserir.exception.IntervaloInvalidoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.inserir.exception.IntervaloNaoInformadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.inserir.exception.PreenchimentoInvalidoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.inserir.exception.QuantidadeReservadaException;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class InserirReservaPorOrgaoUseCaseImpl implements InserirReservaPorOrgaoUseCase {

    private final Clock clock;

    private final ReservaDataProvider reservaDataProvider;

    private final ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    private final ReservaIntervaloNumeroDataProvider reservaIntervaloNumeroDataProvider;

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final InserirReservaPorOrgaoOutputDataConverter outputDataConverter;

    @Override
    public InserirReservaPorOrgaoOutputData executar(InserirReservaPorOrgaoInputData inputData) {
        validarDadosEntrada(inputData);

        patrimonioDataProvider.bloquearEntidade();
        reservaDataProvider.bloquearEntidade();

        Reserva reserva = gerarReserva(inputData);

        verificarReservaValida(reserva, inputData);
        reserva = salvarReserva(reserva);

        cadastrarReservaIntervalo(reserva, inputData);

        return outputDataConverter.to(reserva);
    }

    private void validarDadosEntrada(InserirReservaPorOrgaoInputData inputData) {
        Validator
            .of(inputData)
            .validate(InserirReservaPorOrgaoInputData::getPreenchimento, Objects::nonNull, "Preenchimento é nulo")
            .validate(InserirReservaPorOrgaoInputData::getOrgaoId, Objects::nonNull, "Órgão é nulo")
            .get();
    }

    private Reserva gerarReserva(InserirReservaPorOrgaoInputData inputData) {
        if (Reserva.Preenchimento.AUTOMATICO.name().equals(inputData.getPreenchimento())) {
            return cadastrarReservaAutomatica(inputData);
        } else if (Reserva.Preenchimento.MANUAL.name().equals(inputData.getPreenchimento())) {
            return cadastrarReservaManual(inputData);
        } else {
            throw new PreenchimentoInvalidoException();
        }
    }

    private Reserva cadastrarReservaAutomatica(InserirReservaPorOrgaoInputData inputData) {
        verificarCamposPreenchimentoAutomatica(inputData);

        Long quantidade = inputData.getQuantidadeReservada();
        Long numeroInicio = obterNumeroInicio(inputData);
        Long numeroFim = (numeroInicio + quantidade - 1);
        String proximoCodigo = gerarCodigoReservaFormatado(inputData);

        return Reserva
            .builder()
            .codigo(proximoCodigo)
            .situacao(Reserva.Situacao.FINALIZADO)
            .preenchimento(Reserva.Preenchimento.valueOf(inputData.getPreenchimento()))
            .dataCriacao(LocalDateTime.now(clock))
            .quantidadeReservada(quantidade)
            .quantidadeRestante(quantidade)
            .numeroInicio(numeroInicio)
            .numeroFim(numeroFim)
            .build();
    }

    private void verificarCamposPreenchimentoAutomatica(InserirReservaPorOrgaoInputData inputData) {
        if (!Objects.nonNull(inputData.getQuantidadeReservada())) {
            throw new QuantidadeReservadaException("A quantidade a ser reservada não foi informada");
        }
        if (inputData.getQuantidadeReservada() <= 0) {
            throw new QuantidadeReservadaException("A quantidade a ser reservada deve ser maior que 0");
        }
    }

    private Long obterNumeroInicio(InserirReservaPorOrgaoInputData inputData) {
        Long ultimoNumeroReserva = obterUltimoNumeroReserva(inputData);
        Long ultimoNumeroPatrimonio = obterUltimoNumeroPatrimonioCadastrado(inputData);

        if (ultimoNumeroReserva.compareTo(ultimoNumeroPatrimonio) > 0) {
            return ultimoNumeroReserva + 1;
        }

        return ultimoNumeroPatrimonio + 1;
    }

    private Long obterUltimoNumeroReserva(InserirReservaPorOrgaoInputData inputData) {
        Optional<Reserva> ultimaReservaCadastrada = reservaDataProvider.buscarReservaComMaiorNumeroFimParaOrgao(inputData.getOrgaoId());

        return ultimaReservaCadastrada
            .map(Reserva::getNumeroFim)
            .orElse(0L);
    }

    private Long obterUltimoNumeroPatrimonioCadastrado(InserirReservaPorOrgaoInputData inputData) {
        Optional<Patrimonio> patrimonio = patrimonioDataProvider.buscarPatrimonioComMaiorNumeroPorOrgao(inputData.getOrgaoId());

        return patrimonio
            .map(value -> Long.valueOf(value.getNumero()))
            .orElse(0L);
    }

    private String gerarCodigoReservaFormatado(InserirReservaPorOrgaoInputData inputData) {
        String ultimoCodigoReserva = obterReservaComMaiorCodigoPorOrgao(inputData);

        Long proximoNumero = Long.parseLong(ultimoCodigoReserva) + 1;
        return String.format("%5d", proximoNumero).replace(" ", "0");
    }

    private String obterReservaComMaiorCodigoPorOrgao(InserirReservaPorOrgaoInputData inputData) {
        Optional<Reserva> reserva = reservaDataProvider.buscarReservaComMaiorCodigoPorOrgao(inputData.getOrgaoId());

        return reserva
            .map(Reserva::getCodigo)
            .orElse("0");
    }

    private Reserva cadastrarReservaManual(InserirReservaPorOrgaoInputData inputData) {
        verificarCamposPreenchimentoManual(inputData);

        Long numeroInicial = inputData.getNumeroInicio();
        Long numeroFinal = inputData.getNumeroFim();
        Long quantidade = (numeroFinal - numeroInicial + 1);

        Reserva.Filtro reservaFiltro = Reserva.Filtro
            .builder()
            .orgaoId(inputData.getOrgaoId())
            .numeroInicio(numeroInicial)
            .numeroFim(numeroFinal)
            .build();

        Patrimonio.Filtro patrimonioFiltro = Patrimonio.Filtro
            .builder()
            .orgao(inputData.getOrgaoId())
            .numeroInicio(numeroInicial)
            .numeroFim(numeroFinal)
            .build();

        verificarIntervaloEmUso(reservaFiltro, patrimonioFiltro);
        String proximoCodigo = gerarCodigoReservaFormatado(inputData);

        return Reserva
            .builder()
            .codigo(proximoCodigo)
            .situacao(Reserva.Situacao.FINALIZADO)
            .preenchimento(Reserva.Preenchimento.valueOf(inputData.getPreenchimento()))
            .dataCriacao(LocalDateTime.now(clock))
            .quantidadeReservada(quantidade)
            .quantidadeRestante(quantidade)
            .numeroInicio(numeroInicial)
            .numeroFim(numeroFinal)
            .build();
    }

    private void verificarCamposPreenchimentoManual(InserirReservaPorOrgaoInputData inputData) {
        if (!Objects.nonNull(inputData.getNumeroInicio()) || !Objects.nonNull(inputData.getNumeroFim())) {
            throw new IntervaloNaoInformadoException();
        }
        if (inputData.getNumeroInicio() <= 0 || (inputData.getNumeroFim() < inputData.getNumeroInicio())) {
            throw new IntervaloInvalidoException();
        }
    }

    private void verificarIntervaloEmUso(Reserva.Filtro reservaFiltro, Patrimonio.Filtro patrimonioFiltro) {
        if (reservaDataProvider.existeEmIntervaloPorOrgao(reservaFiltro) ||
            patrimonioDataProvider.existeEmIntervalo(patrimonioFiltro)) {
            throw new IntervaloEmUsoException();
        }
    }

    private void verificarReservaValida(Reserva reserva, InserirReservaPorOrgaoInputData inputData) {
        Reserva.Filtro filtro = Reserva.Filtro
            .builder()
            .numeroInicio(reserva.getNumeroInicio())
            .numeroFim(reserva.getNumeroFim())
            .orgaoId(inputData.getOrgaoId())
            .build();

        if(reservaDataProvider.existeEmIntervaloPorOrgao(filtro)) {
            throw new IntervaloEmUsoException();
        }
    }

    private Reserva salvarReserva(Reserva reserva) {
        return reservaDataProvider.salvar(reserva);
    }

    private void cadastrarReservaIntervalo(Reserva reserva, InserirReservaPorOrgaoInputData inputData) {
        String codigo = gerarCodigoReservaIntervalo(inputData);

        ReservaIntervalo reservaIntervalo = ReservaIntervalo
            .builder()
            .codigo(codigo)
            .reserva(reserva)
            .orgao(UnidadeOrganizacional.builder().id(inputData.getOrgaoId()).build())
            .situacao(ReservaIntervalo.Situacao.FINALIZADO)
            .preenchimento(ReservaIntervalo.Preenchimento.valueOf(
                reserva.getPreenchimento().name()
            ))
            .quantidadeReservada(reserva.getQuantidadeReservada())
            .numeroInicio(reserva.getNumeroInicio())
            .numeroFim(reserva.getNumeroFim())
            .dataFinalizacao(LocalDateTime.now(clock))
            .build();

        ReservaIntervalo reservaIntervaloSalva = reservaIntervaloDataProvider.salvar(reservaIntervalo);

        gerarNumerosReservaIntervalo(reservaIntervaloSalva);
    }

    private String gerarCodigoReservaIntervalo(InserirReservaPorOrgaoInputData inputData) {
        Integer ultimoNumeroIntervaloReserva = buscarUltimoNumeroIntervaloReserva(inputData);

        return String.format("%5d", ultimoNumeroIntervaloReserva+1)
            .replace(" ", "0");
    }

    private Integer buscarUltimoNumeroIntervaloReserva(InserirReservaPorOrgaoInputData inputData) {
        Optional<ReservaIntervalo> reservaIntervalo = reservaIntervaloDataProvider
            .buscarIntervaloComMaiorCodigoPorOrgao(inputData.getOrgaoId());

        return reservaIntervalo
            .map(intervalo -> Integer.valueOf(intervalo.getCodigo()))
            .orElse(0);
    }

    private void gerarNumerosReservaIntervalo(ReservaIntervalo intervalo) {
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

        reservaIntervaloNumeroDataProvider.salvar(numerosIntervalo);
    }
}
