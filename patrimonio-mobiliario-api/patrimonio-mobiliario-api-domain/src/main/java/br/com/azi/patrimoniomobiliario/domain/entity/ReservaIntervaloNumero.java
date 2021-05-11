package br.com.azi.patrimoniomobiliario.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservaIntervaloNumero {
    private Long id;
    private Long numero;
    private Boolean utilizado;
    private ReservaIntervalo reservaIntervalo;
}
