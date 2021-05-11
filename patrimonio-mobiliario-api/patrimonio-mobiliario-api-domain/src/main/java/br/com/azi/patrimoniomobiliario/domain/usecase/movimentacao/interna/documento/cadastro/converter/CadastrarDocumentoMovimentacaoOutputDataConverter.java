package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.cadastro.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Documento;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.cadastro.CadastrarDocumentoMovimentacaoOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;

import java.util.Objects;

public class CadastrarDocumentoMovimentacaoOutputDataConverter extends GenericConverter<Documento, CadastrarDocumentoMovimentacaoOutputData> {

    @Override
    public CadastrarDocumentoMovimentacaoOutputData to(Documento source) {
        final CadastrarDocumentoMovimentacaoOutputData target = super.to(source);

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
