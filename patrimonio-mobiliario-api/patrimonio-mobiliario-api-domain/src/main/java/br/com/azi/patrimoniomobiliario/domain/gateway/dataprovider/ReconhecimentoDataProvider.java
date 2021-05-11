package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Reconhecimento;

import java.util.Optional;

public interface ReconhecimentoDataProvider {

    ListaPaginada<Reconhecimento> buscarPorFiltro(Reconhecimento.Filtro filtro);

    Reconhecimento salvar(Reconhecimento reconhecimento);

    Optional<Reconhecimento> buscarPorId(Long id);

    Reconhecimento atualizar(Reconhecimento reconhecimento);

    boolean existe(Long id);

    Optional<Reconhecimento> buscarReconhecimentoComNome(Long idReconhecimento, String nome);

    boolean existePorNome(String nome);
}
