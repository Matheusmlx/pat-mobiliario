package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;

import java.util.Optional;

public interface IncorporacaoDataProvider {

    Optional<Incorporacao> buscarPorId(Long id);

    Boolean existe(Long id);

    Incorporacao salvar(Incorporacao incorporacao);

    void remover(Long incorporacaoId);

    ListaPaginada<Incorporacao> buscarPorFiltro(Incorporacao.Filtro filtro);

    Boolean existePorId(Long id);
}
