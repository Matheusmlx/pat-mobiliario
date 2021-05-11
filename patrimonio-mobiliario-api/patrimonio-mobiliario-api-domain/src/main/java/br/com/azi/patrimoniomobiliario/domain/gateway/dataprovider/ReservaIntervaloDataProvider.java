package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;

import java.util.List;
import java.util.Optional;

public interface ReservaIntervaloDataProvider {

    List<ReservaIntervalo> buscarPorReserva(Long reservaId);

    List<ReservaIntervalo> buscarPorReservaESituacao(Long id, Reserva.Situacao situacao);

    Optional<ReservaIntervalo> buscarPorId(Long reservaIntervaloId);

    Boolean existeIntervalosPorReserva(Reserva reserva);

    Boolean existe(Long reservaId, ReservaIntervalo.Situacao situacao);

    Boolean existe(Long reservaIntervaloId);

    Optional<ReservaIntervalo> buscarIntervaloComMaiorNumeroReserva(Long reservaId);

    Optional<ReservaIntervalo> buscarIntervaloComMaiorNumeroTermoResponsabilidade();

    List<ReservaIntervalo> salvar(List<ReservaIntervalo> reservaIntervalos);

    ReservaIntervalo salvar(ReservaIntervalo reservaIntervalo);

    Boolean existeIntervaloNaReserva(Long numeroInicio, Long numeroFim, Long reservaId);

    Optional<ReservaIntervalo> retornaIntervaloComNumero(Integer numero, Long reservaId);

    ListaPaginada<ReservaIntervalo> buscarIntervalosPorReserva(ReservaIntervalo.Filtro filtro);

    void remover(Long id);

    void remover(List<Long> ids);

    Optional<ReservaIntervalo> buscarReservaIntervaloComMaiorCodigo();

    Optional<ReservaIntervalo> buscarIntervaloComMaiorCodigoPorOrgao(Long orgaoId);

    void bloquearEntidade();
}
