package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.EditaItemIncorporacaoOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.Objects;

public class EditaItemIncorporacaoOutputDataConverter extends GenericConverter<ItemIncorporacao, EditaItemIncorporacaoOutputData> {

    @Override
    public EditaItemIncorporacaoOutputData to(ItemIncorporacao source) {
        EditaItemIncorporacaoOutputData target = super.to(source);

        if(Objects.nonNull(source.getContaContabil())){
            target.setContaContabil(EditaItemIncorporacaoOutputData
                .ContaContabil
                .builder()
                    .id(source.getContaContabil().getId())
                    .codigo(source.getContaContabil().getCodigo())
                    .descricao(source.getContaContabil().getDescricao())
                .build());
        }

        if(Objects.nonNull(source.getNaturezaDespesa())){
            target.setNaturezaDespesa(EditaItemIncorporacaoOutputData
                .NaturezaDespesa
                 .builder()
                .id(source.getNaturezaDespesa().getId())
                .descricao(source.getNaturezaDespesa().getDescricao())
                .despesa(source.getNaturezaDespesa().getDespesa())
                .situacao(EditaItemIncorporacaoOutputData
                    .NaturezaDespesa.Situacao.valueOf(source.getNaturezaDespesa().getSituacao().name()))
                .build());
        }

        if(Objects.nonNull(source.getEstadoConservacao())) {
            target.setEstadoConservacao(source.getEstadoConservacao().getId());
        }
        return target;
    }
}
