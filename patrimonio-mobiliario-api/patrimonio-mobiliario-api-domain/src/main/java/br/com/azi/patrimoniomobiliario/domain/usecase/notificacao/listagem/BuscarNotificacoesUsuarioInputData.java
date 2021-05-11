package br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.listagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarNotificacoesUsuarioInputData {
    private Long page;
}
