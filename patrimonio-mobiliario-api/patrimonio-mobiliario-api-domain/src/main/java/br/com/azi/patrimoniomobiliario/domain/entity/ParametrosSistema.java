package br.com.azi.patrimoniomobiliario.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParametrosSistema {
    private String urlChatSuporte;
    private String quantidadeDigitosNumeroPatrimonio;
    private Boolean reservaPatrimonialGlobal;
}
