package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.TipoDocumento;

import java.util.List;
import java.util.Optional;

public interface TipoDocumentoDataprovider {
    List<TipoDocumento> buscar();

    Optional<TipoDocumento> buscarPorId(Long id);
}
