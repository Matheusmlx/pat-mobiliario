package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.estadoconservacao.listagem;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.EstadoConservacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.estadoconservacao.listagem.converter.EstadoConservacaoOutputDataConverter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BuscarEstadosConservacaoUseCaseImpl implements BuscarEstadosConservacaoUseCase {

    private EstadoConservacaoDataProvider estadoConservacaoDataProvider;

    private EstadoConservacaoOutputDataConverter outputDataConverter;

    @Override
    public BuscarEstadosConservacaoOutputData executar() {
        return outputDataConverter.to(estadoConservacaoDataProvider.buscarEstadosConservacao());
    }
}
