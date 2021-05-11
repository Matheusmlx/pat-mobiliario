package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.remocao.vazia;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.remocao.vazia.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.remocao.vazia.exception.MovimentacaoNaoEstaEmElaboracaoException;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class RemoverMovimentacaoInternaVaziaUseCaseImpl implements RemoverMovimentacaoInternaVaziaUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    @Override
    public void executar(RemoverMovimentacaoInternaVaziaInputData inputData) {
        validarDadosEntrada(inputData);

        final Movimentacao movimentacao = buscar(inputData.getMovimentacaoId());
        verificarMovimentacaoEmModoElaboracao(movimentacao);

        remover(inputData);
    }

    private void validarDadosEntrada(RemoverMovimentacaoInternaVaziaInputData inputData) {
        Validator.of(inputData)
            .validate(RemoverMovimentacaoInternaVaziaInputData::getMovimentacaoId, Objects::nonNull, "id da movimentação é nulo")
            .get();
    }

    private Movimentacao buscar(Long movimentacaoId) {
        Optional<Movimentacao> movimentacao = movimentacaoDataProvider.buscarPorId(movimentacaoId);
        return movimentacao.orElseThrow(MovimentacaoNaoEncontradaException::new);
    }

    private void verificarMovimentacaoEmModoElaboracao(Movimentacao movimentacao) {
        if (!Movimentacao.Situacao.EM_ELABORACAO.equals(movimentacao.getSituacao()) &&
            !Movimentacao.Situacao.ERRO_PROCESSAMENTO.equals(movimentacao.getSituacao())) {
            throw new MovimentacaoNaoEstaEmElaboracaoException();
        }
    }

    private void remover(RemoverMovimentacaoInternaVaziaInputData inputData) {
        movimentacaoDataProvider.removerPorId(inputData.getMovimentacaoId());
    }
}
