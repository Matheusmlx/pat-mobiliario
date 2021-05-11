package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.editar.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Documento;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.editar.EditarDocumentoOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.Objects;

public class EditarDocumentoOutputDataConverter extends GenericConverter<Documento, EditarDocumentoOutputData> {
    @Override
    public EditarDocumentoOutputData to(Documento source) {
        EditarDocumentoOutputData target = super.to(source);
        if (Objects.nonNull(source.getIncorporacao().getId())) {
            target.setIncorporacao(source.getIncorporacao().getId());
        }
        if (Objects.nonNull(source.getTipo().getId())) {
            target.setTipo(source.getTipo().getId());
        }
        return target;
    }
}
