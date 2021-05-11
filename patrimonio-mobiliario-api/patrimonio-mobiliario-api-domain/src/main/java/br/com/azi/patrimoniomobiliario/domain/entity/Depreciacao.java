package br.com.azi.patrimoniomobiliario.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Depreciacao {
    private Long id;
    private BigDecimal valorAnterior;
    private BigDecimal valorSubtraido;
    private BigDecimal valorPosterior;
    private BigDecimal taxaAplicada;
    private LocalDateTime dataInicial;
    private LocalDateTime dataFinal;
    private String mesReferencia;
    private Patrimonio patrimonio;
    private UnidadeOrganizacional orgao;
    private UnidadeOrganizacional setor;
    private ContaContabil contaContabil;
}
