package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.estadoconservacao.listagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.EstadoConservacao;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.estadoconservacao.listagem.BuscarEstadosConservacaoOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.List;
import java.util.stream.Collectors;

public class EstadoConservacaoOutputDataConverter {

    public BuscarEstadosConservacaoOutputData to(List<EstadoConservacao> source) {
        BuscarEstadoConservacaoOutputDataConverter outputDataConverter = new BuscarEstadoConservacaoOutputDataConverter();

        return BuscarEstadosConservacaoOutputData
            .builder()
            .estadosConservacao(source
                .stream()
                .map(outputDataConverter::to)
                .collect(Collectors.toList()))
            .build();
    }

    public static class BuscarEstadoConservacaoOutputDataConverter extends GenericConverter<EstadoConservacao, BuscarEstadosConservacaoOutputData.Item> {
    }
}
