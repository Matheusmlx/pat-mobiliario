package br.com.azi.patrimoniomobiliario.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Documento {
    private Long id;
    private String numero;
    private LocalDateTime data;
    private BigDecimal valor;
    private String uriAnexo;
    private Incorporacao incorporacao;
    private Movimentacao movimentacao;
    private TipoDocumento tipo;
}
