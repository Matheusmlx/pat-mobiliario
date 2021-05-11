package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.remocao;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.remocao.exception.MovimentacaoItemNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.remocao.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.remocao.exception.MovimentacaoNaoEstaEmElaboracaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.remocao.exception.PatrimonioNaoEncontradoException;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class RemoverItemMovimentacaoUseCaseImpl implements RemoverItemMovimentacaoUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final PatrimonioDataProvider patrimonioDataProvider;

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    @Override
    public void executar(RemoverItemMovimentacaoInputData inputData) {
        validarDadosEntrada(inputData);

        final Movimentacao movimentacao = buscarMovimentacao(inputData);
        verificarMovimentacaoEmModoElaboracao(movimentacao);

        verificarExistenciaPatrimonio(inputData);

        MovimentacaoItem movimentacaoItem = buscarMovimentacaoItem(inputData);

        remover(movimentacaoItem);
    }

    private void validarDadosEntrada(RemoverItemMovimentacaoInputData inputData) {
        Validator.of(inputData)
            .validate(RemoverItemMovimentacaoInputData::getMovimentacaoId, Objects::nonNull, "Id da movimentação é nulo")
            .validate(RemoverItemMovimentacaoInputData::getPatrimonioId, Objects::nonNull, "Id do patrimônio é nulo")
            .get();
    }

    private Movimentacao buscarMovimentacao(RemoverItemMovimentacaoInputData inputData) {
        return movimentacaoDataProvider.buscarPorId(inputData.getMovimentacaoId())
            .orElseThrow(MovimentacaoNaoEncontradaException::new);
    }

    private void verificarMovimentacaoEmModoElaboracao(Movimentacao movimentacao) {
        if (!Movimentacao.Situacao.EM_ELABORACAO.equals(movimentacao.getSituacao()) &&
            !Movimentacao.Situacao.ERRO_PROCESSAMENTO.equals(movimentacao.getSituacao())) {
            throw new MovimentacaoNaoEstaEmElaboracaoException();
        }
    }

    private void verificarExistenciaPatrimonio(RemoverItemMovimentacaoInputData inputData) {
        if (!patrimonioDataProvider.existe(inputData.getPatrimonioId())) {
            throw new PatrimonioNaoEncontradoException();
        }
    }

    private MovimentacaoItem buscarMovimentacaoItem(RemoverItemMovimentacaoInputData inputData) {
        return movimentacaoItemDataProvider.buscarPorMovimentacaoEPatrimonio(inputData.getMovimentacaoId(), inputData.getPatrimonioId())
            .orElseThrow(MovimentacaoItemNaoEncontradaException::new);
    }

    private void remover(MovimentacaoItem movimentacaoItem) {
        movimentacaoItemDataProvider.remover(movimentacaoItem);
    }
}
