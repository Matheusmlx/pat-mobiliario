package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervaloNumero;

import java.util.List;

public interface ReservaIntervaloNumeroDataProvider {

    ReservaIntervaloNumero salvar(ReservaIntervaloNumero reservaIntervaloNumero);

    List<ReservaIntervaloNumero> salvar(List<ReservaIntervaloNumero> reservaIntervalos);

    boolean existePorIntervalo(Long reservaIntervaloId);

    boolean existePorIntervalo(List<Long> reservaIntervaloIds);

    boolean existeNumeroUtilizadoPorIntervalo(List<Long> reservaIntervaloIds);

    void removerPorIntervaloId(List<Long> reservaIntervaloIds);

    List<ReservaIntervaloNumero> buscarPorIntervalo(Long reservaIntervaloId);

    Boolean existeUtilizadoPorIntervalo(List<Long> reservaIntervaloId);

}
