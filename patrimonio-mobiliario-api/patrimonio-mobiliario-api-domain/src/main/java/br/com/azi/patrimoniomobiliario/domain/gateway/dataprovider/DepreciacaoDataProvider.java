package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.Depreciacao;

import java.util.List;

import java.time.LocalDateTime;
import java.util.Optional;

public interface DepreciacaoDataProvider {

    Depreciacao salvar(Depreciacao depreciacao);

    void salvar(List<Depreciacao> depreciacoes);

    List<Depreciacao> buscarDepreciacoesPorPatrimonioId(Long patrimonioId);

    Boolean existePorPatrimonioNoPeriodo(Long patrimonioId, LocalDateTime mesReferencia);

    Optional<Depreciacao> buscarUltimaPorPatrimonio(Long patrimonioId);

    Boolean existePorPatrimonio(Long patrimonioId);

}
