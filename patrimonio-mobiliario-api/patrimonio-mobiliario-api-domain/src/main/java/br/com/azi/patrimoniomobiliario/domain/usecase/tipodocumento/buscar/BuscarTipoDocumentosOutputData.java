package br.com.azi.patrimoniomobiliario.domain.usecase.tipodocumento.buscar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarTipoDocumentosOutputData {

    List<TipoDocumento> items;

    @Data
    @NoArgsConstructor
    public static class TipoDocumento {
        private Long id;
        private String descricao;
    }
}
