package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.visualizacao;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoItemDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.visualizacao.converter.BuscarInformacaoDevolucaoPatrimoniosOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.visualizacao.exception.MovimentacaoNaoEncontrada;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.visualizacao.exception.SituacaoMovimentacaoException;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class BuscarInformacaoDevolucaoPatrimoniosUseCaseImpl implements BuscarInformacaoDevolucaoPatrimoniosUseCase {

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final MovimentacaoItemDataProvider movimentacaoItemDataProvider;

    private final BuscarInformacaoDevolucaoPatrimoniosOutputDataConverter outputDataConverter;

    @Override
    public BuscarInformacaoDevolucaoPatrimoniosOutputData executar(BuscarInformacaoDevolucaoPatrimoniosInputData inputData) {
        validarDadosEntrada(inputData);

        Movimentacao movimentacao = buscar(inputData);
        verificarSituacaoMovimentacao(movimentacao);

        String razaoPatrimoniosDevolvidos = obterComparacaoDevolucaoPatrimonios(inputData);

        return outputDataConverter.to(razaoPatrimoniosDevolvidos);
    }

    private void validarDadosEntrada(BuscarInformacaoDevolucaoPatrimoniosInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarInformacaoDevolucaoPatrimoniosInputData::getId, Objects::nonNull, "Id da movimentação é nulo")
            .get();
    }

    private Movimentacao buscar(BuscarInformacaoDevolucaoPatrimoniosInputData inputData) {
        return movimentacaoDataProvider.buscarPorId(inputData.getId())
            .orElseThrow(MovimentacaoNaoEncontrada::new);
    }

    private void verificarSituacaoMovimentacao(Movimentacao movimentacao) {
        if (!Movimentacao.Situacao.DEVOLVIDO_PARCIAL.equals(movimentacao.getSituacao())) {
            throw new SituacaoMovimentacaoException();
        }
    }

    private String obterComparacaoDevolucaoPatrimonios(BuscarInformacaoDevolucaoPatrimoniosInputData inputData) {
        Long quantidadePatrimoniosDevolvidos = movimentacaoItemDataProvider.buscarQuantidadeItensDevolvidos(inputData.getId());
        Long quantidadeTotalPatrimonios = movimentacaoItemDataProvider.buscarQuantidadeItensPorMovimentacaoId(inputData.getId());

        return String.format("%d/%d patrimônios devolvidos", quantidadePatrimoniosDevolvidos, quantidadeTotalPatrimonios);
    }

}
