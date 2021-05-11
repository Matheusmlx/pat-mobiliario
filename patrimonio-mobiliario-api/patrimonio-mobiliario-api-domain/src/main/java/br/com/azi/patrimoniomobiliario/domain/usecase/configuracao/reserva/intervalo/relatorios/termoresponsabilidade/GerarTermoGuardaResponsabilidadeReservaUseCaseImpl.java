package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.entity.TermoGuardaResponsabilidadeReservaPatrimonial;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReservaIntervaloDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeRelatoriosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade.converter.GerarTermoGuardaResponsabilidadeReservaOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade.exception.IntervaloNaoPertenceReservaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade.exception.ReservaIntervaloNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade.exception.ReservaIntervaloNaoFinalizadaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade.exception.ReservaNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.utils.string.StringUtils;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class GerarTermoGuardaResponsabilidadeReservaUseCaseImpl implements GerarTermoGuardaResponsabilidadeReservaUseCase {

    private final Clock clock;

    private final ReservaDataProvider reservaDataProvider;

    private final ReservaIntervaloDataProvider reservaIntervaloDataProvider;

    private final SistemaDeRelatoriosIntegration sistemaDeRelatoriosIntegration;

    private final GerarTermoGuardaResponsabilidadeReservaOutputDataConverter outputDataConverter;

    @Override
    public GerarTermoGuardaResponsabilidadeReservaOutputData executar(GerarTermoGuardaResponsabilidadeReservaInputData inputData) {
        validarDadosEntrada(inputData);
        Reserva reserva = buscarReserva(inputData);
        ReservaIntervalo reservaIntervalo = buscarReservaIntervalo(inputData);

        verificarReservaPossuiIntervalo(reserva, reservaIntervalo);
        verificarSeIntervaloFinalizado(reservaIntervalo);

        Arquivo arquivo = sistemaDeRelatoriosIntegration.gerarTermoGuardaResponsabilidadeReservaPatrimonial(montarTermoResponsabilidade(reservaIntervalo));

        return outputDataConverter.to(arquivo);
    }

    private void validarDadosEntrada(GerarTermoGuardaResponsabilidadeReservaInputData inputData) {
        Validator.of(inputData)
            .validate(GerarTermoGuardaResponsabilidadeReservaInputData::getReservaId, Objects::nonNull, "Id da reserva é nulo")
            .validate(GerarTermoGuardaResponsabilidadeReservaInputData::getReservaIntervaloId, Objects::nonNull, "Id do intervalo é nulo")
            .get();
    }

    private Reserva buscarReserva(GerarTermoGuardaResponsabilidadeReservaInputData inputData) {
        return reservaDataProvider.buscarPorId(inputData.getReservaId())
            .orElseThrow(ReservaNaoEncontradaException::new);
    }

    private ReservaIntervalo buscarReservaIntervalo(GerarTermoGuardaResponsabilidadeReservaInputData inputData) {
        return reservaIntervaloDataProvider.buscarPorId(inputData.getReservaIntervaloId())
            .orElseThrow(ReservaIntervaloNaoEncontradaException::new);
    }

    private void verificarReservaPossuiIntervalo(Reserva reserva, ReservaIntervalo reservaIntervalo) {
        if (!reservaIntervalo.getReserva().getId().equals(reserva.getId())) {
            throw new IntervaloNaoPertenceReservaException();
        }
    }

    private void verificarSeIntervaloFinalizado(ReservaIntervalo intervalo) {
        if(!intervalo.getSituacao().equals(ReservaIntervalo.Situacao.FINALIZADO)) {
            throw new ReservaIntervaloNaoFinalizadaException();
        }
    }

    private TermoGuardaResponsabilidadeReservaPatrimonial montarTermoResponsabilidade(ReservaIntervalo reservaIntervalo){
        return TermoGuardaResponsabilidadeReservaPatrimonial
            .builder()
            .numeroTermo(gerarNumeroTermoResponsabilidade(reservaIntervalo)+"/"+obterAnoAtual())
            .orgao(configurarUnidadeOrganizacional(reservaIntervalo.getOrgao()))
            .codigo(reservaIntervalo.getCodigo())
            .dataCriacao(formatarDataReserva(reservaIntervalo))
            .preenchimento(obterTipoPreenchimento(reservaIntervalo))
            .quantidade(reservaIntervalo.getQuantidadeReservada())
            .intervalo(reservaIntervalo.getNumeroInicio() + " - " + reservaIntervalo.getNumeroFim())
            .build();
    }

    private String configurarUnidadeOrganizacional(UnidadeOrganizacional unidadeOrganizacional) {
        return unidadeOrganizacional.getSigla()+" - "+unidadeOrganizacional.getNome();
    }

    private String formatarDataReserva(ReservaIntervalo reservaIntervalo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return reservaIntervalo.getDataFinalizacao().format(formatter);
    }

    private String gerarNumeroTermoResponsabilidade(ReservaIntervalo intervalo) {
        String numeroTermo = intervalo.getNumeroTermo();
        if(Objects.isNull(numeroTermo)) {
            Integer proximoNumeroTermo = obterProximoNumeroTermoResponsabilidadeReserva();
            String numeroTermoFormatado = formatarNumeroTermo(proximoNumeroTermo);

            intervalo.setNumeroTermo(numeroTermoFormatado);
            salvarIntervalo(intervalo);

            return numeroTermoFormatado;
        }
        return numeroTermo;
    }

    private Integer obterProximoNumeroTermoResponsabilidadeReserva() {
        Optional<ReservaIntervalo> reservaIntervalo = reservaIntervaloDataProvider.buscarIntervaloComMaiorNumeroTermoResponsabilidade();
        return reservaIntervalo
            .map(intervalo -> Integer.parseInt(intervalo.getNumeroTermo()) + 1)
            .orElse(1);
    }

    private String formatarNumeroTermo(Integer numero) {
        return StringUtils.padLeftZeros(String.valueOf(numero), Integer.parseInt("6"));
    }

    private void salvarIntervalo(ReservaIntervalo reservaIntervalo) {
        reservaIntervaloDataProvider.salvar(reservaIntervalo);
    }

    private String obterAnoAtual() {
        return String.valueOf(LocalDate.now(clock).getYear());
    }

    private String obterTipoPreenchimento(ReservaIntervalo reservaIntervalo) {
        switch (reservaIntervalo.getPreenchimento()) {
            case AUTOMATICO:
                return "Automático";
            case MANUAL:
                return "Manual";
            default:
                return "-";
        }
    }

}
