package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.buscar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarDocumentosMovimentacaoOutputData {

    private List<BuscarDocumentosMovimentacaoOutputData.Documento> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Documento {
        private Long id;
        private String numero;
        private String data;
        private BigDecimal valor;
        private String uriAnexo;
        private Long tipo;
        private Long movimentacao;
    }
}
