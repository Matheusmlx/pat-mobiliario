package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Fornecedor;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.FornecedorEntity;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class FornecedorConverter extends GenericConverter<FornecedorEntity, Fornecedor> {

    @Override
    public Fornecedor to(FornecedorEntity source) {
        Fornecedor target = super.to(source);

        if (Objects.nonNull(source.getRazaoSocial())) {
            target.setNome(source.getRazaoSocial());
        }
        return target;
    }
}
