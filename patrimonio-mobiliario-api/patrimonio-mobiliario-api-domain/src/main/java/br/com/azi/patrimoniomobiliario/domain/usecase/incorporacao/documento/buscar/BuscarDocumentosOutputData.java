package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.buscar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarDocumentosOutputData {

    List<Documento> items;

    @Data
    @NoArgsConstructor
    public static class Documento {
        private Long id;
        private String numero;
        private String data;
        private BigDecimal valor;
        private String uriAnexo;
        private Long tipo;
        private Long incorporacao;
    }
}
