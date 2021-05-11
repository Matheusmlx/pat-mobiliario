package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.listagem;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.listagem.exception.FiltroIncompletoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.listagem.converter.BuscarItensMovimentacaoFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.listagem.converter.BuscarItensMovimentacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.listagem.exception.MovimentacaoNaoEncontradaException;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class BuscarItensMovimentacaoUseCaseImpl implements BuscarItensMovimentacaoUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private final BuscarItensMovimentacaoFiltroConverter filtroConverter;

    private final BuscarItensMovimentacaoOutputDataConverter outputDataConverter;

    @Override
    public BuscarItensMovimentacaoOutputData executar(BuscarItensMovimentacaoInputData inputData) {
        validarDadosEntrada(inputData);
        validarMovimentacaoExistente(inputData.getMovimentacaoId());

        final MovimentacaoItem.Filtro filtro = criarFiltro(inputData);
        final ListaPaginada<MovimentacaoItem> entidadesEncontradas = buscarItensMovimentacao(filtro);

        return outputDataConverter.to(entidadesEncontradas);
    }

    private void validarDadosEntrada(BuscarItensMovimentacaoInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarItensMovimentacaoInputData::getMovimentacaoId, Objects::nonNull, "O id da movimentação é nulo.")
            .validate(BuscarItensMovimentacaoInputData::getPage, page -> Objects.nonNull(page) && page >= 0, new FiltroIncompletoException("Ausência do número da página."))
            .validate(BuscarItensMovimentacaoInputData::getSize, size -> Objects.nonNull(size) && size > 0, new FiltroIncompletoException("Ausência do tamanho da página."))
            .get();
    }

    private void validarMovimentacaoExistente(Long movimentacaoId) {
        if (!movimentacaoDataProvider.existe(movimentacaoId)) {
            throw new MovimentacaoNaoEncontradaException();
        }
    }

    private MovimentacaoItem.Filtro criarFiltro(BuscarItensMovimentacaoInputData inputData) {
        return filtroConverter.to(inputData);
    }

    private ListaPaginada<MovimentacaoItem> buscarItensMovimentacao(MovimentacaoItem.Filtro filtro) {
        return movimentacaoItemDataProvider.buscarPorMovimentacaoId(filtro);
    }

}
