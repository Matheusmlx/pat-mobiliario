package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.Concedente;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;

import java.util.Optional;

public interface ConcedenteDataProvider {

    Concedente salvar(Concedente concedente);

    Optional<Concedente> buscarPorId(Long id);

    Boolean existePorCpf(String cpfCnpj);

    ListaPaginada<Concedente> buscarPorFiltro(Concedente.Filtro filtro);

    Boolean existe(Long id);
}
