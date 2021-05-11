package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ConfigContaContabil;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ConfigContaContabilEntity;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ConfigContaContabilConverter extends GenericConverter<ConfigContaContabilEntity, ConfigContaContabil> {

    @Autowired
    ContaContabilConverter contaContabilConverter;

    @Override
    public ConfigContaContabilEntity from(ConfigContaContabil source) {
        ConfigContaContabilEntity target = super.from(source);

        if (Objects.nonNull(source.getContaContabil())) {
            target.setContaContabil(contaContabilConverter.from(source.getContaContabil()));
        }

        return target;
    }

    @Override
    public ConfigContaContabil to(ConfigContaContabilEntity source) {
        ConfigContaContabil target = super.to(source);

        if (Objects.nonNull(source.getContaContabil())) {
            target.setContaContabil(contaContabilConverter.to(source.getContaContabil()));
        }

        return target;
    }
}
