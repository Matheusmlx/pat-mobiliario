package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.remover;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemoverDocumentoMovimentacaoInputData {
    private Long id;
    private Long movimentacao;
}
