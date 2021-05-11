package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.TipoDocumento;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.TipoDocumentoEntity;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import org.springframework.stereotype.Component;

@Component
public class TipoDocumentoConverter extends GenericConverter<TipoDocumentoEntity, TipoDocumento> {

    @Override
    public TipoDocumento to(TipoDocumentoEntity tipoDocumentoEntity) {
        TipoDocumento tipoDocumento = super.to(tipoDocumentoEntity);
        tipoDocumento.setId(tipoDocumentoEntity.getId());

        tipoDocumento.setDescricao(tipoDocumentoEntity.getDescricao());
        tipoDocumento.setIdentificacaoDocumento(tipoDocumentoEntity.getIdentificacaoDocumento());
        tipoDocumento.setPermiteAnexo(tipoDocumentoEntity.getPermiteAnexo());
        return tipoDocumento;
    }

}
