package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.exclusao;

import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.exclusao.exception.IncorporacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.exclusao.exception.ItemIncorporacaoNaoPodeSerExcluidoException;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class ExcluirItemIncorporacaoUseCaseImpl implements ExcluirItemIncorporacaoUseCase {

    private final ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    private final IncorporacaoDataProvider incorporacaoDataProvider;

    @Override
    public void executar(ExcluirItemIncorporacaoInputData inputData) {
        validarDadosEntrada(inputData);
        Incorporacao incorporacao = buscarIncorporacao(inputData);
        validarSituacaoIncorporacao(incorporacao);
        remover(inputData);
    }

    private void validarDadosEntrada(ExcluirItemIncorporacaoInputData inputData) {
        Validator.of(inputData)
            .validate(ExcluirItemIncorporacaoInputData::getId, Objects::nonNull, "O id do item de incorporação é nulo")
            .validate(ExcluirItemIncorporacaoInputData::getIdIncorporacao, Objects::nonNull, "O id da incorporação é nulo")
            .get();
    }

    private Incorporacao buscarIncorporacao(ExcluirItemIncorporacaoInputData inputData) {
        Optional<Incorporacao> incorporacao = incorporacaoDataProvider.buscarPorId(inputData.getIdIncorporacao());
        return incorporacao.orElseThrow(IncorporacaoNaoEncontradaException::new);
    }

    private void validarSituacaoIncorporacao(Incorporacao incorporacao) {
        if (!Incorporacao.Situacao.EM_ELABORACAO.equals(incorporacao.getSituacao()) &&
            !Incorporacao.Situacao.ERRO_PROCESSAMENTO.equals(incorporacao.getSituacao())) {
            throw new ItemIncorporacaoNaoPodeSerExcluidoException();
        }
    }

    private void remover(ExcluirItemIncorporacaoInputData inputData) {
        itemIncorporacaoDataProvider.excluir(inputData.getId());
    }
}
