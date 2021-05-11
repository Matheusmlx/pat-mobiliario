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
public class ConfigContaContabil {

    private Long id;
    private Metodo metodo;
    private Tipo tipo;
    private TipoBem tipoBem;
    private ContaContabil contaContabil;
    private Integer vidaUtil;
    private BigDecimal percentualResidual;
    private Situacao situacao;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAlteracao;
    private String usuarioCadastro;
    private String usuarioAlteracao;

    public enum Metodo {
        QUOTAS_CONSTANTES,
        SOMA_DOS_DIGITOS
    }

    public enum Situacao {
        ATIVO,
        INATIVO
    }

    public enum Tipo {
        DEPRECIAVEL,
        NAO_DEPRECIAVEL
    }

    public enum TipoBem {
        ARMAMENTO,
        EQUIPAMENTO,
        MOVEL,
        VEICULO
    }

}
