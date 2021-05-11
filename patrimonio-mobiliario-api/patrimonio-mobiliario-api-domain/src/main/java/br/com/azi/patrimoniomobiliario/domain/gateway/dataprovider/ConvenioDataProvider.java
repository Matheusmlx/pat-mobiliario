package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.Convenio;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;

import java.util.Optional;

public interface ConvenioDataProvider {
    Convenio salvar(Convenio convenio);

    Optional<Convenio> buscarPorId(Long id);

    ListaPaginada<Convenio> buscarPorFiltro(Convenio.Filtro filtro);

    Convenio atualizar(Convenio convenio);

    boolean existePorNumero(String numero);

    boolean existe(Long id);
}
