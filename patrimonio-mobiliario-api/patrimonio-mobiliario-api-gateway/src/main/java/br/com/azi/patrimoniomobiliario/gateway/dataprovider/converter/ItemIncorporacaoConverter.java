package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ConfiguracaoDepreciacaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ItemIncorporacaoEntity;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ItemIncorporacaoConverter extends GenericConverter<ItemIncorporacaoEntity, ItemIncorporacao> {

    @Autowired
    NaturezaDespesaConverter naturezaDespesaConverter;

    @Autowired
    ContaContabilConverter contaContabilConverter;

    @Autowired
    IncorporacaoConverter incorporacaoConverter;

    @Autowired
    ConfiguracaoDepreciacaoConverter configuracaoDepreciacaoConverter;

    @Autowired
    EstadoConservacaoConverter estadoConservacaoConverter;

    @Override
    public ItemIncorporacaoEntity from(ItemIncorporacao source) {
        ItemIncorporacaoEntity target = super.from(source);

        if (Objects.nonNull(source.getContaContabil())) {
            target.setContaContabil(contaContabilConverter.from(source.getContaContabil()));
        }

        if (Objects.nonNull(source.getNaturezaDespesa())) {
            target.setNaturezaDespesa(naturezaDespesaConverter.from(source.getNaturezaDespesa()));
        }

        if (Objects.nonNull(source.getIncorporacao())) {
            target.setIncorporacao(incorporacaoConverter.from(source.getIncorporacao()));
        }

        if (Objects.nonNull(source.getEstadoConservacao())) {
            target.setEstadoConservacao(estadoConservacaoConverter.from(source.getEstadoConservacao()));
        }

        if (Objects.nonNull(source.getConfiguracaoDepreciacao())) {
            target.setConfigDepreciacao(ConfiguracaoDepreciacaoEntity
                .builder()
                .id(source.getConfiguracaoDepreciacao().getId())
                .build());
        }

        return target;
    }

    @Override
    public ItemIncorporacao to(ItemIncorporacaoEntity source) {
        ItemIncorporacao target = super.to(source);

        if (Objects.nonNull(source.getIncorporacao())) {
            target.setIdIncorporacao(source.getIncorporacao().getId());
        }

        if (Objects.nonNull(source.getContaContabil())) {
            target.setContaContabil(contaContabilConverter.to(source.getContaContabil()));
        }

        if (Objects.nonNull(source.getConfigDepreciacao())) {
            target.setConfiguracaoDepreciacao(configuracaoDepreciacaoConverter.to(source.getConfigDepreciacao()));
        }

        if (Objects.nonNull(source.getNaturezaDespesa())) {
            target.setNaturezaDespesa(naturezaDespesaConverter.to(source.getNaturezaDespesa()));
        }

        if (Objects.nonNull(source.getIncorporacao())) {
            target.setIncorporacao(incorporacaoConverter.to(source.getIncorporacao()));
        }

        if (Objects.nonNull(source.getEstadoConservacao())) {
            target.setEstadoConservacao(estadoConservacaoConverter.to(source.getEstadoConservacao()));
        }

        return target;
    }
}
