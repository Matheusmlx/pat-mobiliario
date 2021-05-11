package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.buscarporid.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.buscarporid.BuscarItemIncorporacaoPorIdOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.Objects;

public class BuscarItemIncorporacaoPorIdOutputDataConverter extends GenericConverter<ItemIncorporacao, BuscarItemIncorporacaoPorIdOutputData> {

    @Override
    public BuscarItemIncorporacaoPorIdOutputData to(ItemIncorporacao source) {
        BuscarItemIncorporacaoPorIdOutputData target = super.to(source);

        if (Objects.nonNull(source.getContaContabil())) {
            target.setContaContabil(BuscarItemIncorporacaoPorIdOutputData
                .ContaContabil
                .builder()
                .id(source.getContaContabil().getId())
                .codigo(source.getContaContabil().getCodigo())
                .descricao(source.getContaContabil().getDescricao())
                .build());
        }

        if (Objects.nonNull(source.getNaturezaDespesa())) {
            target.setNaturezaDespesa(BuscarItemIncorporacaoPorIdOutputData
                .NaturezaDespesa
                .builder()
                .id(source.getNaturezaDespesa().getId())
                .descricao(source.getNaturezaDespesa().getDescricao())
                .despesa(source.getNaturezaDespesa().getDespesa())
                .situacao(BuscarItemIncorporacaoPorIdOutputData
                    .NaturezaDespesa.Situacao.valueOf(source.getNaturezaDespesa().getSituacao().name()))
                .build());

        }

        if(Objects.nonNull(source.getEstadoConservacao())) {
            target.setEstadoConservacao(source.getEstadoConservacao().getId());
        }
        return target;
    }

}
