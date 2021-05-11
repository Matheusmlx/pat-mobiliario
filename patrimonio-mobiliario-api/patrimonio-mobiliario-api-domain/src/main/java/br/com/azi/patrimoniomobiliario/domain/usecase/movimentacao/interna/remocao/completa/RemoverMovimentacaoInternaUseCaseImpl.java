package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.remocao.completa;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.remocao.completa.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.remocao.completa.exception.MovimentacaoNaoEstaEmElaboracaoException;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class RemoverMovimentacaoInternaUseCaseImpl implements RemoverMovimentacaoInternaUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final DocumentoDataProvider documentoDataProvider;

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private final NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    private final LancamentoContabilDataProvider lancamentoContabilDataProvider;

    @Override
    public void executar(RemoverMovimentacaoInternaInputData inputData) {
        validarDadosEntrada(inputData);

        final Movimentacao movimentacao = buscar(inputData);
        verificarMovimentacaoEmModoElaboracao(movimentacao);

        removerMovimentacaoItemVinculadosMovimentacao(movimentacao);
        removerDocumentosVinculadosMovimentacao(movimentacao);
        removerLancamentosContabeisVinculadosMovimentacao(movimentacao);

        removerMovimentacao(movimentacao);
        removerNotaLancamentoContabilVinculadoMovimentacao(movimentacao);
    }

    private void validarDadosEntrada(RemoverMovimentacaoInternaInputData inputData) {
        Validator.of(inputData)
            .validate(RemoverMovimentacaoInternaInputData::getMovimentacaoId, Objects::nonNull, "Id da movimentação é nulo")
            .get();
    }

    private Movimentacao buscar(RemoverMovimentacaoInternaInputData inputData) {
        Optional<Movimentacao> movimentacao = movimentacaoDataProvider.buscarPorId(inputData.getMovimentacaoId());
        return movimentacao.orElseThrow(MovimentacaoNaoEncontradaException::new);
    }

    private void verificarMovimentacaoEmModoElaboracao(Movimentacao movimentacao) {
        if (!Movimentacao.Situacao.EM_ELABORACAO.equals(movimentacao.getSituacao()) &&
            !Movimentacao.Situacao.ERRO_PROCESSAMENTO.equals(movimentacao.getSituacao())) {
            throw new MovimentacaoNaoEstaEmElaboracaoException();
        }
    }

    private void removerMovimentacaoItemVinculadosMovimentacao(Movimentacao movimentacao) {
        movimentacaoItemDataProvider.removerPorMovimentacao(movimentacao.getId());
    }

    private void removerNotaLancamentoContabilVinculadoMovimentacao(Movimentacao movimentacao) {
        if (Objects.nonNull(movimentacao.getNotaLancamentoContabil())) {
            notaLancamentoContabilDataProvider.remover(movimentacao.getNotaLancamentoContabil().getId());
        }
    }

    private void removerDocumentosVinculadosMovimentacao(Movimentacao movimentacao) {
        documentoDataProvider.removerPorMovimentacao(movimentacao.getId());
    }

    private void removerLancamentosContabeisVinculadosMovimentacao(Movimentacao movimentacao) {
        lancamentoContabilDataProvider.removerPorMovimentacao(movimentacao.getId());
    }

    private void removerMovimentacao(Movimentacao movimentacao) {
        movimentacaoDataProvider.remover(movimentacao);
    }

}
