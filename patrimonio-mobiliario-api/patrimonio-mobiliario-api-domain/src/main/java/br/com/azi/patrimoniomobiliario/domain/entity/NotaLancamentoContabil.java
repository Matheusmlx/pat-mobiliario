package br.com.azi.patrimoniomobiliario.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotaLancamentoContabil {
    private Long id;
    private String numero;
    private LocalDateTime dataLancamento;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAlteracao;
    private String usuarioCadastro;
    private String usuarioAlteracao;
}
