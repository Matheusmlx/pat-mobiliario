package br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.removerantigas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoverNotificacoesAntigasOutputData {
    private String dataReferencia;
    private int quantidadeRemovidas;
}
