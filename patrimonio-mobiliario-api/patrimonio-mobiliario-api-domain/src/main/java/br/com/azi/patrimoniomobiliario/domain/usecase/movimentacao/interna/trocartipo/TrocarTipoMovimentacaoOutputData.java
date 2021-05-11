package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.trocartipo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrocarTipoMovimentacaoOutputData {
    private Long id;
    private String codigo;
    private String usuarioCriacao;
    private String tipo;
    private String situacao;
}
