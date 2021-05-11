package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditarReservaPatrimonialInputData {
    private Long id;
    private String preenchimento;
    private Long quantidadeReservada;
    private Long numeroInicio;
    private Long numeroFim;
}
