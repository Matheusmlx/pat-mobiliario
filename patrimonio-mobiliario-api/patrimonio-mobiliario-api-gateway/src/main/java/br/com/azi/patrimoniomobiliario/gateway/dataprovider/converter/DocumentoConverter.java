package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Documento;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.TipoDocumento;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.DocumentoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.IncorporacaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.MovimentacaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.TipoDocumentoEntity;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DocumentoConverter extends GenericConverter<DocumentoEntity, Documento> {

    @Override
    public DocumentoEntity from(Documento source) {
        DocumentoEntity documentoEntity = super.from(source);

        if (Objects.nonNull(source.getData())) {
            documentoEntity.setData(DateUtils.asDate(source.getData()));
        }

        if (Objects.nonNull(source.getIncorporacao())) {
            IncorporacaoEntity incorporacaoEntity = new IncorporacaoEntity();
            incorporacaoEntity.setId(source.getIncorporacao().getId());
            documentoEntity.setIncorporacao(incorporacaoEntity);
        }
        if (Objects.nonNull(source.getMovimentacao())) {
            documentoEntity.setMovimentacao(MovimentacaoEntity.builder()
                .id(source.getMovimentacao().getId())
                .build());
        }
        if (Objects.nonNull(source.getTipo())) {
            TipoDocumentoEntity tipoDocumentoEntity = new TipoDocumentoEntity();
            tipoDocumentoEntity.setId(source.getTipo().getId());
            documentoEntity.setTipoDocumento(tipoDocumentoEntity);
        }

        return documentoEntity;
    }

    @Override
    public Documento to(DocumentoEntity documentoEntity) {
        Documento documento = super.to(documentoEntity);

        if (Objects.nonNull(documentoEntity.getData())) {
            documento.setData(DateUtils.asLocalDateTime(documentoEntity.getData()));
        }
        documento.setIncorporacao(incorporacaoJpaEntitytoIncorporacaoBusinessEntity(documentoEntity.getIncorporacao()));
        documento.setMovimentacao(movimentacaoJpaEntityToMovimentacaoBusinessEntity(documentoEntity.getMovimentacao()));
        documento.setTipo(tipoDocumentoJpaEntitytoTipoDocumentoBusinessEntity(documentoEntity.getTipoDocumento()));
        return documento;
    }

    private Incorporacao incorporacaoJpaEntitytoIncorporacaoBusinessEntity(IncorporacaoEntity jpa) {
        if (Objects.nonNull(jpa)) {
            return Incorporacao.builder()
                .id(jpa.getId())
                .build();
        }
        return null;
    }

    private Movimentacao movimentacaoJpaEntityToMovimentacaoBusinessEntity(MovimentacaoEntity jpa) {
        if (Objects.nonNull(jpa)) {
            return Movimentacao.builder()
                .id(jpa.getId())
                .build();
        }
        return null;
    }

    private TipoDocumento tipoDocumentoJpaEntitytoTipoDocumentoBusinessEntity(TipoDocumentoEntity jpa) {
        if (Objects.nonNull(jpa)) {
            return TipoDocumento.builder()
                .id(jpa.getId())
                .build();
        }
        return null;
    }

}
