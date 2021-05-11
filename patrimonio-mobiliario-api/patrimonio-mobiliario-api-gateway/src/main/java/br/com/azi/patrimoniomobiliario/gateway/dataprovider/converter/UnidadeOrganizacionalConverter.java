package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.UnidadeOrganizacionalEntity;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UnidadeOrganizacionalConverter extends GenericConverter<UnidadeOrganizacionalEntity, UnidadeOrganizacional> {

    @Override
    public UnidadeOrganizacional to(UnidadeOrganizacionalEntity source) {
        UnidadeOrganizacional target = super.to(source);

        target.setDescricao(String.format("%s - %s", source.getSigla(), source.getNome()));

        if(Objects.nonNull(source.getCodigoHierarquia())) {
            target.setCodHierarquia(source.getCodigoHierarquia());
        }

        return target;
    }

}
