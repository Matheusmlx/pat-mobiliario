package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagem;

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
public class BuscarPatrimoniosPorItemIncorporacaoOutputData {
    private List<Item> items;
    private Long totalPages;
    private Long totalElements;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private Long id;
        private Boolean diferencaDizima;
        private String marca;
        private String modelo;
        private String fabricante;
        private String estadoConservacao;
        private String numero;
        private BigDecimal valorLiquido;
        private BigDecimal valorResidual;
        private BigDecimal valorAquisicao;
        private String tipo;
        private String anoFabricacaoModelo;
        private String placa;
        private String renavam;
        private String licenciamento;
        private String motor;
        private String chassi;
        private String numeroSerie;
        private String imagem;
        private Long contaContabil;
    }
}
