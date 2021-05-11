package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.edicao.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Documento;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.edicao.EditarDocumentoMovimentacaoOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;

import java.util.Objects;

public class EditarDocumentoMovimentacaoOutputDataConverter extends GenericConverter<Documento, EditarDocumentoMovimentacaoOutputData> {

    @Override
    public EditarDocumentoMovimentacaoOutputData to(Documento source) {
        EditarDocumentoMovimentacaoOutputData target = super.to(source);

        if (Objects.nonNull(source.getMovimentacao())) {
            target.setMovimentacao(source.getMovimentacao().getId());
        }

        if (Objects.nonNull(source.getTipo())) {
            target.setTipo(source.getTipo().getId());
        }

        if (Objects.nonNull(source.getData())) {
            target.setData(DateValidate.formatarData(source.getData()));
        }

        return target;
    }

}
