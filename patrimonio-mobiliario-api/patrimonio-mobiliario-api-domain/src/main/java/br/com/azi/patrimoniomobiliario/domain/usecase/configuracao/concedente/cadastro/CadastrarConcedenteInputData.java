package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.cadastro;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CadastrarConcedenteInputData {
    private String cpfCnpj;
    private String nome;
    private String situacao;
}
