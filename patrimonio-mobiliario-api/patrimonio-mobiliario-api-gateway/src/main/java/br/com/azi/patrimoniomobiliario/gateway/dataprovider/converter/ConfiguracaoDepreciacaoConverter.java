package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ConfiguracaoDepreciacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ConfiguracaoDepreciacaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ContaContabilEntity;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ConfiguracaoDepreciacaoConverter extends GenericConverter<ConfiguracaoDepreciacaoEntity, ConfiguracaoDepreciacao> {

    @Override
    public ConfiguracaoDepreciacaoEntity from(ConfiguracaoDepreciacao source) {
        ConfiguracaoDepreciacaoEntity target = super.from(source);

        if (Objects.nonNull(source.getContaContabil())) {
            target.setContaContabil(ContaContabilEntity
                .builder()
                .id(source.getContaContabil().getId())
                .build());
        }

        return target;
    }

    @Override
    public ConfiguracaoDepreciacao to(ConfiguracaoDepreciacaoEntity source) {
        ConfiguracaoDepreciacao target = super.to(source);

        if (Objects.nonNull(source.getContaContabil())) {
            target.setContaContabil(ContaContabil
                .builder()
                .id(source.getContaContabil().getId())
                .build());
        }

        return target;
    }
}
