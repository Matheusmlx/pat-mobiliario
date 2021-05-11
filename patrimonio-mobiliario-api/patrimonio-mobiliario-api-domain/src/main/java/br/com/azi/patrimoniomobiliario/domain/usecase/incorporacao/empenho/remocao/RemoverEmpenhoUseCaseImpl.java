package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.remocao;

import br.com.azi.patrimoniomobiliario.domain.entity.Empenho;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.EmpenhoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.remocao.exception.EmpenhoNaoExisteException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.remocao.exception.IncorporacaoNaoEstaEmElaboracaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.remocao.exception.IncorporacaoNaoExisteException;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class RemoverEmpenhoUseCaseImpl implements RemoverEmpenhoUseCase {

    private final EmpenhoDataProvider empenhoDataProvider;

    private final IncorporacaoDataProvider incorporacaoDataProvider;

    @Override
    public void executar(RemoverEmpenhoInputData inputData) {
        validarDadosEntrada(inputData);

        final Empenho empenho = buscarEmpenho(inputData);
        final Incorporacao incorporacao = buscarIncorporacao(empenho.getIncorporacaoId());
        validarIncorporacaoEmModoElaboracao(incorporacao);

        remover(empenho);
    }

    public void validarDadosEntrada(RemoverEmpenhoInputData inputData) {
        Validator.of(inputData)
            .validate(RemoverEmpenhoInputData::getId, Objects::nonNull, "Id é número")
            .get();
    }

    private Empenho buscarEmpenho(RemoverEmpenhoInputData inputData) {
        return empenhoDataProvider.buscarPorId(inputData.getId())
            .orElseThrow(EmpenhoNaoExisteException::new);
    }

    private Incorporacao buscarIncorporacao(Long incorporacaoId) {
        return incorporacaoDataProvider.buscarPorId(incorporacaoId)
            .orElseThrow(IncorporacaoNaoExisteException::new);
    }

    private void validarIncorporacaoEmModoElaboracao(Incorporacao incorporacao) {
        if (!Incorporacao.Situacao.EM_ELABORACAO.equals(incorporacao.getSituacao()) &&
            !Incorporacao.Situacao.ERRO_PROCESSAMENTO.equals(incorporacao.getSituacao())) {
            throw new IncorporacaoNaoEstaEmElaboracaoException();
        }
    }

    private void remover(Empenho empenho) {
        empenhoDataProvider.remover(empenho.getId());
    }
}
