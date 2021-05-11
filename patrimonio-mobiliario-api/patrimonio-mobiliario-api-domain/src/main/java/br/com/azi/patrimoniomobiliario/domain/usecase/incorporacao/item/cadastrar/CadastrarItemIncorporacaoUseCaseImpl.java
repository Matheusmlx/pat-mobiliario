package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.cadastrar;

import br.com.azi.patrimoniomobiliario.domain.entity.EstadoConservacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.EstadoConservacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.cadastrar.converter.CadastrarItemIncorporacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.cadastrar.exception.IncorporacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.cadastrar.exception.IncorporacaoNaoEstaEmElaboracaoException;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class CadastrarItemIncorporacaoUseCaseImpl implements CadastrarItemIncorporacaoUseCase {

    private final ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    private final IncorporacaoDataProvider incorporacaoDataProvider;

    private final EstadoConservacaoDataProvider estadoConservacaoDataProvider;

    private final CadastrarItemIncorporacaoOutputDataConverter outputDataConverter;

    @Override
    public CadastrarItemIncorporacaoOutputData executar(CadastrarItemIncorporacaoInputData inputData) {
        validaEntrada(inputData);

        final Incorporacao incorporacao = buscarIncorporacao(inputData);
        validarIncorporacaoEmModoElaboracao(incorporacao);

        Boolean execucaoOrcamentaria = obterExecucaoOrcamentaria(inputData);
        ItemIncorporacao itemIncorporacao = criaEntidade(inputData, execucaoOrcamentaria);
        ItemIncorporacao itemIncorporacaoSalvo = salvar(itemIncorporacao);

        return outputDataConverter.to(itemIncorporacaoSalvo);
    }

    private void validaEntrada(CadastrarItemIncorporacaoInputData inputData) {
        Validator.of(inputData)
            .validate(CadastrarItemIncorporacaoInputData::getDescricao, Objects::nonNull, "Item não possui descrição!")
            .validate(CadastrarItemIncorporacaoInputData::getCodigo, Objects::nonNull, "Item deve possuir código.")
            .get();
    }

    private Incorporacao buscarIncorporacao(CadastrarItemIncorporacaoInputData inputData) {
        return incorporacaoDataProvider.buscarPorId(inputData.getIncorporacaoId())
            .orElseThrow(IncorporacaoNaoEncontradaException::new);
    }

    private void validarIncorporacaoEmModoElaboracao(Incorporacao incorporacao) {
        if (!Incorporacao.Situacao.EM_ELABORACAO.equals(incorporacao.getSituacao()) &&
            !Incorporacao.Situacao.ERRO_PROCESSAMENTO.equals(incorporacao.getSituacao())) {
            throw new IncorporacaoNaoEstaEmElaboracaoException();
        }
    }

    private Boolean obterExecucaoOrcamentaria(CadastrarItemIncorporacaoInputData inputData) {
        Optional<Incorporacao> incorporacaoEncontrada = incorporacaoDataProvider.buscarPorId(inputData.getIncorporacaoId());
        Incorporacao incorporacao = incorporacaoEncontrada.orElseThrow(IncorporacaoNaoEncontradaException::new);
        return incorporacao.getReconhecimento().getExecucaoOrcamentaria();
    }

    private ItemIncorporacao criaEntidade(CadastrarItemIncorporacaoInputData inputData, Boolean execucaoOrcamentaria) {
        ItemIncorporacao itemIncorporacao = ItemIncorporacao
            .builder()
            .descricao(inputData.getDescricao())
            .codigo(inputData.getCodigo())
            .situacao(ItemIncorporacao.Situacao.EM_ELABORACAO)
            .incorporacao(Incorporacao.builder().id(inputData.getIncorporacaoId()).build())
            .build();

        if (execucaoOrcamentaria) {
            EstadoConservacao estadoConservacao = obterMelhorEstadoConservacao();

            if (Objects.nonNull(estadoConservacao)) {
                itemIncorporacao.setEstadoConservacao(estadoConservacao);
            }
        }

        return itemIncorporacao;
    }

    private EstadoConservacao obterMelhorEstadoConservacao() {
        return estadoConservacaoDataProvider.obterMelhorEstadoConservacao();
    }

    private ItemIncorporacao salvar(ItemIncorporacao itemIncorporacao) {
        return itemIncorporacaoDataProvider.salvar(itemIncorporacao);
    }

}
