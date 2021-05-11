package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.edicao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditarReconhecimentoInputData {
    private Long id;
    private String nome;
    private String situacao;
    private Boolean execucaoOrcamentaria;
    private Boolean notaFiscal;
    private Boolean empenho;
}
