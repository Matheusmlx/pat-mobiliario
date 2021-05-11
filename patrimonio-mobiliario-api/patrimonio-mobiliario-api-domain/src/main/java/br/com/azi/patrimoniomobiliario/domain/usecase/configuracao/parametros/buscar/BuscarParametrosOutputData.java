package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.parametros.buscar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuscarParametrosOutputData {
    private String urlChatSuporte;
    private String quantidadeDigitosNumeroPatrimonio;
    private Boolean reservaPatrimonialGlobal;
}
