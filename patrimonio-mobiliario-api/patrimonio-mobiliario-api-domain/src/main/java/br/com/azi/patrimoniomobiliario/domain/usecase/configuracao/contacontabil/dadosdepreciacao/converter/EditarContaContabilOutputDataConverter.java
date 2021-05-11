package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.converter;


import br.com.azi.patrimoniomobiliario.domain.entity.ConfigContaContabil;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.EditarContaContabilOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.Objects;

public class EditarContaContabilOutputDataConverter  extends GenericConverter<ConfigContaContabil, EditarContaContabilOutputData> {

    @Override
    public EditarContaContabilOutputData to(ConfigContaContabil source) {
        EditarContaContabilOutputData target = super.to(source);

        if (Objects.nonNull(source.getContaContabil())) {
            target.setContaContabil(source.getContaContabil().getId());
        }
        return target;
    }
}
