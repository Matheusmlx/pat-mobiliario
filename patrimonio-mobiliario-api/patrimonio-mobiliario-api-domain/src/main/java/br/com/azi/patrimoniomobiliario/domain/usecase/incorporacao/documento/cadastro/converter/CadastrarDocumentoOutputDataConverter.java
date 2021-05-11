package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Documento;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.CadastrarDocumentoOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;

import java.util.Objects;

public class CadastrarDocumentoOutputDataConverter extends GenericConverter<Documento, CadastrarDocumentoOutputData> {

    @Override
    public CadastrarDocumentoOutputData to(Documento source) {
        CadastrarDocumentoOutputData target = super.to(source);
        if (Objects.nonNull(source.getIncorporacao().getId())) {
            target.setIncorporacao(source.getIncorporacao().getId());
        }
        if (Objects.nonNull(source.getTipo())) {
            target.setTipo(source.getTipo().getId());
        }
        if(source.getData()!=null){
            target.setData(DateValidate.formatarData(source.getData()));
        }

        return target;
    }
}
