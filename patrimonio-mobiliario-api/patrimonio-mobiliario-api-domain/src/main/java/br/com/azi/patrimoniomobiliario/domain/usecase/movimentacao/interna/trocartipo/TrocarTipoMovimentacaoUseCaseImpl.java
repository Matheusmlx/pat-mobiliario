package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.trocartipo;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.MovimentacaoConstants;
import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.trocartipo.converter.TrocarTipoMovimentacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.trocartipo.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.trocartipo.exception.MovimentacaoNaoEstaEmElaboracaoException;
import br.com.azi.patrimoniomobiliario.utils.string.StringUtils;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class TrocarTipoMovimentacaoUseCaseImpl implements TrocarTipoMovimentacaoUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private final DocumentoDataProvider documentoDataProvider;

    private final SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    private final NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    private final TrocarTipoMovimentacaoOutputDataConverter converter;

    @Override
    public TrocarTipoMovimentacaoOutputData executar(TrocarTipoMovimentacaoInputData inputData) {
        validarDadosEntrada(inputData);

        final Movimentacao movimentacaoExistente = buscar(inputData.getId());
        validarMovimentacaoEmModoElaboracao(movimentacaoExistente);

        if (verificarMesmoTipoMovimentacao(inputData.getTipo(), movimentacaoExistente.getTipo())) {
            return converter.to(movimentacaoExistente);
        }

        removerItensMovimentacao(movimentacaoExistente.getId());
        removerDocumentosMovimentacao(movimentacaoExistente.getId());
        removerNotaLancamentoContabilVinculada(movimentacaoExistente.getNotaLancamentoContabil());
        removerMovimentacao(movimentacaoExistente);

        final Movimentacao novaMovimentacao = criarNovaMovimentacaoComNovoTipo(inputData.getTipo());
        final Movimentacao movimentacaoSalva = salvarMovimentacao(novaMovimentacao);

        return converter.to(movimentacaoSalva);
    }

    private void validarDadosEntrada(TrocarTipoMovimentacaoInputData inputData) {
        Validator.of(inputData)
            .validate(TrocarTipoMovimentacaoInputData::getId, Objects::nonNull, "Id da movimentação é nulo.")
            .validate(TrocarTipoMovimentacaoInputData::getTipo, StringUtils::isNotEmpty, "Tipo da movimentação é nulo ou vazio.")
            .get();
    }

    private Movimentacao buscar(Long id) {
        Optional<Movimentacao> movimentacao = movimentacaoDataProvider.buscarPorId(id);
        return movimentacao.orElseThrow(MovimentacaoNaoEncontradaException::new);
    }

    private void validarMovimentacaoEmModoElaboracao(Movimentacao movimentacaoExistente) {
        if (!Movimentacao.Situacao.EM_ELABORACAO.equals(movimentacaoExistente.getSituacao()) &&
            !Movimentacao.Situacao.ERRO_PROCESSAMENTO.equals(movimentacaoExistente.getSituacao())) {
            throw new MovimentacaoNaoEstaEmElaboracaoException();
        }
    }

    private boolean verificarMesmoTipoMovimentacao(String tipoEscolhido, TipoMovimentacaoEnum tipoAtual) {
        return tipoAtual.equals(TipoMovimentacaoEnum.valueOf(tipoEscolhido));
    }

    private void removerItensMovimentacao(Long movimentacaoId) {
        if (movimentacaoItemDataProvider.existePorMovimentacaoId(movimentacaoId)) {
            movimentacaoItemDataProvider.removerPorMovimentacao(movimentacaoId);
        }
    }

    private void removerDocumentosMovimentacao(Long movimentacaoId) {
        if (documentoDataProvider.existePorMovimentacaoId(movimentacaoId)) {
            documentoDataProvider.removerPorMovimentacao(movimentacaoId);
        }
    }

    private void removerNotaLancamentoContabilVinculada(NotaLancamentoContabil notaLancamentoContabil) {
        if (Objects.nonNull(notaLancamentoContabil)) {
            notaLancamentoContabilDataProvider.remover(notaLancamentoContabil.getId());
        }
    }

    private void removerMovimentacao(Movimentacao movimentacaoExistente) {
        movimentacaoDataProvider.remover(movimentacaoExistente);
    }

    private Movimentacao criarNovaMovimentacaoComNovoTipo(String tipo) {
        return Movimentacao.builder()
            .situacao(Movimentacao.Situacao.EM_ELABORACAO)
            .tipo(TipoMovimentacaoEnum.valueOf(tipo))
            .codigo(gerarCodigoMovimentacao())
            .usuarioCriacao(sessaoUsuarioDataProvider.getLogin())
            .build();
    }

    private String gerarCodigoMovimentacao() {
        final String ultimoCodigoCadastrado = movimentacaoDataProvider.buscarUltimoCodigoCadastrado();
        final int proximoCodigoSequencial = Integer.parseInt(ultimoCodigoCadastrado) + 1;
        final int quantidadeDigitos = MovimentacaoConstants.QUANTIDADE_DIGITOS_CODIGO_SEQUENCIAL;
        return String.format(("%" + quantidadeDigitos + "s"), proximoCodigoSequencial).replace(' ', '0');
    }

    private Movimentacao salvarMovimentacao(Movimentacao novaMovimentacao) {
        return movimentacaoDataProvider.salvar(novaMovimentacao);
    }
}
