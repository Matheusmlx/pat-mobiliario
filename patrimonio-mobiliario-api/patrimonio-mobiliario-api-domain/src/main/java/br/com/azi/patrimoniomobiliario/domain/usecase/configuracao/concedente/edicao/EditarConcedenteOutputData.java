package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.edicao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditarConcedenteOutputData {
    private Long id;
    private String cpfCnpj;
    private String tipoPessoa;
    private String nome;
    private String situacao;
}
