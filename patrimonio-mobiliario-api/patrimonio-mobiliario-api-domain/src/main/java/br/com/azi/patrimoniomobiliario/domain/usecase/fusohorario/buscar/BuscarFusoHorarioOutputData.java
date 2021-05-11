package br.com.azi.patrimoniomobiliario.domain.usecase.fusohorario.buscar;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BuscarFusoHorarioOutputData {
    String fusoHorario;
}
