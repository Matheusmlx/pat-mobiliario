package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.Empenho;

import java.util.List;
import java.util.Optional;

public interface EmpenhoDataProvider {
    Optional<Empenho> buscarPorId(Long id);

    Empenho salvar(Empenho empenho);

    Boolean existePorId(Long id);

    List<Empenho> buscarPorIncorporacao(Empenho.Filtro filtro);

    void remover(Long id);

    Long retornaQuantidadePorIncorporacaoId(Long incorporacaoId);
}
