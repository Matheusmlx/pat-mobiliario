package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.buscar.conveter;

import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.buscar.BuscarContasContabeisInputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

public class BuscarContasContabeisFiltroConverter extends GenericConverter<BuscarContasContabeisInputData, ContaContabil.Filtro> {

    @Override
    public ContaContabil.Filtro to(BuscarContasContabeisInputData source) {
        ContaContabil.Filtro target = super.to(source);
        target.setPage(target.getPage() - 1);
        return target;
    }

}
