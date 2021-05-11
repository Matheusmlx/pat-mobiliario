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
public class ConfiguracaoDepreciacao {

    private Long id;
    private ContaContabil contaContabil;
    private Integer vidaUtil;
    private Metodo metodo;
    private BigDecimal percentualResidual;
    private Boolean depreciavel;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAlteracao;
    private String usuarioCadastro;
    private String usuarioAlteracao;

    public enum Metodo {
        QUOTAS_CONSTANTES
    }

}
