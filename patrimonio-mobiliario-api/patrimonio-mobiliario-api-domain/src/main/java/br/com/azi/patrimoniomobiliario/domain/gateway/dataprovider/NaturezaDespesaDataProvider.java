package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.NaturezaDespesa;

import java.util.List;
import java.util.Optional;

public interface NaturezaDespesaDataProvider {

    List<NaturezaDespesa> buscarPorFiltro(Long materialServicoId);

    Optional<NaturezaDespesa> buscarPorId(Long id);
}
