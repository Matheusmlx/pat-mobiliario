package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.NaturezaDespesa;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.NaturezaDespesaEntity;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class NaturezaDespesaConverter extends GenericConverter<NaturezaDespesaEntity, NaturezaDespesa> {

    @Autowired
    private ContaContabilConverter contaContabilConverter;

    @Override
    public NaturezaDespesaEntity from(NaturezaDespesa source) {
        NaturezaDespesaEntity target = super.from(source);

        if (Objects.nonNull(source.getContaContabil())) {
            target.setContaContabil(contaContabilConverter.from(source.getContaContabil()));
        }


        return target;
    }

    @Override
    public NaturezaDespesa to(NaturezaDespesaEntity source) {
        NaturezaDespesa target = super.to(source);
        if (Objects.nonNull(source.getContaContabil())) {
            target.setContaContabil(contaContabilConverter.to(source.getContaContabil()));
        }
        return target;
    }

}
