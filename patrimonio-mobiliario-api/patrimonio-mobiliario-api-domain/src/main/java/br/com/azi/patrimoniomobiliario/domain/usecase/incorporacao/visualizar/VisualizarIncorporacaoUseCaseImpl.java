package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.visualizar;

import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.visualizar.converter.VisualizarIncorporacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.visualizar.exception.IncorporacaoNaoEncontradaException;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class VisualizarIncorporacaoUseCaseImpl implements VisualizarIncorporacaoUseCase {

    private IncorporacaoDataProvider incorporacaoDataProvider;

    private PatrimonioDataProvider patrimonioDataProvider;

    private VisualizarIncorporacaoOutputDataConverter outputDataConverter;

    @Override
    public VisualizarIncorporacaoOutputData executar(VisualizarIncorporacaoInputData inputData) {
        validarDadosEntrada(inputData);

        Incorporacao incorporacaoEncontrada = buscar(inputData);
        final boolean possuiPatrimoniosEmOutrasMovimentacoes = verificarPatrimoniosEmOutrasMovimentacoes(inputData);

        return outputDataConverter.to(incorporacaoEncontrada, possuiPatrimoniosEmOutrasMovimentacoes);
    }

    private void validarDadosEntrada(VisualizarIncorporacaoInputData inputData) {
        Validator.of(inputData)
            .validate(VisualizarIncorporacaoInputData::getId, Objects::nonNull, "Id é nulo")
            .get();
    }

    private Incorporacao buscar(VisualizarIncorporacaoInputData inputData) {
        Optional<Incorporacao> entidade = incorporacaoDataProvider.buscarPorId(inputData.getId());
        return entidade.orElseThrow(IncorporacaoNaoEncontradaException::new);
    }

    private boolean verificarPatrimoniosEmOutrasMovimentacoes(VisualizarIncorporacaoInputData inputData) {
        final Long quantidadePatrimoniosEmOutrasMovimentacoes = patrimonioDataProvider
            .buscarQuantidadePatrimoniosEmOutrasMovimentacoesPorIncorporacaoId(inputData.getId());

        return quantidadePatrimoniosEmOutrasMovimentacoes > 0;
    }
}
