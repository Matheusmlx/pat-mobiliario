package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.buscarlistagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Reconhecimento;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.buscarlistagem.BuscarReconhecimentosInputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

public class BuscarReconhecimentosFiltroConverter extends GenericConverter<BuscarReconhecimentosInputData, Reconhecimento.Filtro> {

    @Override
    public Reconhecimento.Filtro to(BuscarReconhecimentosInputData from) {
        Reconhecimento.Filtro target = super.to(from);
        target.setPage(target.getPage() - 1);
        return target;
    }
}
