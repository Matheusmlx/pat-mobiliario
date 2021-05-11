package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.cadastrar;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CadastrarItemIncorporacaoInputData {
    private String descricao;
    private String codigo;
    private Long incorporacaoId;
}
