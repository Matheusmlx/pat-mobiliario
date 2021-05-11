package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;

import java.util.Optional;

public interface ReservaDataProvider {
    Optional<Reserva> buscarPorId(Long id);

    Optional<Reserva> buscarPorId(Long id, Reserva.Filtro filtro);

    Optional<Reserva> buscarReservaComMaiorNumeroFim();

    Optional<Reserva> buscarReservaComMaiorNumeroFimParaOrgao(Long orgaoId);

    Optional<Reserva> buscarReservaComMaiorCodigoPorOrgao(Long orgaoId);

    Optional<Reserva> buscarReservaComMaiorNumeroDiferenteDe(Reserva reserva);

    Optional<Reserva> buscarReservaComMaiorCodigo();

    Boolean existe(Long id);

    Boolean existeEmIntervalo(Reserva.Filtro filtro);

    Boolean existeEmIntervaloPorOrgao(Reserva.Filtro filtro);

    Boolean existeEmIntervaloDeOutraReserva(Long numeroInicio, Long numeroFim, Long reservaId);

    Reserva salvar(Reserva reserva);

    ListaPaginada<Reserva> buscarReservas(Reserva.Filtro filtro);

    void remover(Long id);

    void bloquearEntidade();
}
