package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscartipo;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscartipo.converter.BuscarTipoMovimentacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscartipo.exception.MovimentacaoNaoEncontradaException;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class BuscarTipoMovimentacaoUseCaseImpl implements BuscarTipoMovimentacaoUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;
    private final BuscarTipoMovimentacaoOutputDataConverter outputDataConverter;

    @Override
    public BuscarTipoMovimentacaoOutputData executar(BuscarTipoMovimentacaoInputData inputData) {
        validarDadosEntrada(inputData);

        Movimentacao movimentacao = buscar(inputData);

        return outputDataConverter.to(movimentacao);
    }

    private void validarDadosEntrada(BuscarTipoMovimentacaoInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarTipoMovimentacaoInputData::getMovimentacaoId, Objects::nonNull, "Id da movimentação é nulo")
            .get();
    }

    private Movimentacao buscar(BuscarTipoMovimentacaoInputData inputData) {
        Optional<Movimentacao> movimentacao = movimentacaoDataProvider.buscarPorId(inputData.getMovimentacaoId());
        return movimentacao.orElseThrow(MovimentacaoNaoEncontradaException::new);
    }
}
