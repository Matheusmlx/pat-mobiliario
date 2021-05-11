package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.EditaItemIncorporacaoInputData;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ItemIncorporacaoDataProvider {
    ListaPaginada<ItemIncorporacao> buscarPorIncorporacaoId(ItemIncorporacao.Filtro filtro);

    Optional<ItemIncorporacao> buscarPorId(Long id);

    void excluir(Long incorporacaoId);

    Boolean existe(Long id);

    Boolean existeItemComMesmaNatureza(EditaItemIncorporacaoInputData inputData);

    ItemIncorporacao atualizar(ItemIncorporacao item);

    ItemIncorporacao salvar(ItemIncorporacao item);

    void salvarTodos(List<ItemIncorporacao> itens);

    BigDecimal buscaSomaDeValorTotalDosItens(Long incorporacaoId);

    List<ItemIncorporacao> buscarItensPorIncorporacaoId(Long incorporacaoId);
}
