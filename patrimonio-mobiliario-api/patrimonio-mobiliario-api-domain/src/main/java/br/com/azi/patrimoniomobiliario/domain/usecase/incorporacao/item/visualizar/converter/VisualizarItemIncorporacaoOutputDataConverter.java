package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.visualizar.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.visualizar.VisualizarItemIncorporacaoOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.Objects;

public class VisualizarItemIncorporacaoOutputDataConverter extends GenericConverter<ItemIncorporacao, VisualizarItemIncorporacaoOutputData> {

    @Override
    public VisualizarItemIncorporacaoOutputData to(ItemIncorporacao source) {
        VisualizarItemIncorporacaoOutputData target = super.to(source);

       if (Objects.nonNull(source.getContaContabil())) {
           target.setContaContabilDescricao(source.getContaContabil().getCodigo()+" - "+source.getContaContabil().getDescricao());
       }

        if (Objects.nonNull(source.getEstadoConservacao())) {
            target.setEstadoConservacaoNome(source.getEstadoConservacao().getNome());
        }

        return target;
    }
}
