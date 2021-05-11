package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.remocao;

import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.remocao.exception.IncorporacaoNaoEncontradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.remocao.exception.IncorporacaoNaoPodeSerExcluidoException;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class RemoverIncorporacaoUseCaseImpl implements RemoverIncorporacaoUseCase {

    private final IncorporacaoDataProvider incorporacaoDataProvider;

    private final NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    @Override
    public void executar(RemoverIncorporacaoInputData inputData) {
        validarDadosEntrada(inputData);
        Incorporacao incorporacao = buscarIncorporacao(inputData);
        validarSituacaoIncorporacao(incorporacao);
        remover(inputData);
        excluirNotaLancamentoContabil(incorporacao);
    }

    private void validarDadosEntrada(RemoverIncorporacaoInputData inputData) {
        Validator.of(inputData)
            .validate(RemoverIncorporacaoInputData::getId, Objects::nonNull, "O id da incorporação é nulo")
            .get();
    }

    private Incorporacao buscarIncorporacao(RemoverIncorporacaoInputData inputData) {
        Optional<Incorporacao> incorporacao = incorporacaoDataProvider.buscarPorId(inputData.getId());
        return incorporacao.orElseThrow(IncorporacaoNaoEncontradoException::new);
    }

    private void validarSituacaoIncorporacao(Incorporacao incorporacao) {
        if (Incorporacao.Situacao.FINALIZADO.equals(incorporacao.getSituacao()) ||
            Incorporacao.Situacao.EM_PROCESSAMENTO.equals(incorporacao.getSituacao())) {
            throw new IncorporacaoNaoPodeSerExcluidoException();
        }
    }

    private void remover(RemoverIncorporacaoInputData inputData) {
        incorporacaoDataProvider.remover(inputData.getId());
    }

    private void excluirNotaLancamentoContabil(Incorporacao incorporacao) {
        if (Objects.nonNull(incorporacao.getNotaLancamentoContabil())) {
            notaLancamentoContabilDataProvider.remover(incorporacao.getNotaLancamentoContabil().getId());
        }
    }

}
