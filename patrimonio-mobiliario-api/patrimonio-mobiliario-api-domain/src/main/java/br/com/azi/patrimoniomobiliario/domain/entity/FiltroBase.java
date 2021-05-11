package br.com.azi.patrimoniomobiliario.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FiltroBase {
    Long page;
    Long size;
    String sort;
    String direction;
}
