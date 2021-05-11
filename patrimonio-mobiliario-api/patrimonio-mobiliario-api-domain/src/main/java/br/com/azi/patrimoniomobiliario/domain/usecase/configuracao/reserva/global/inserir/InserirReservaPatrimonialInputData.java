package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InserirReservaPatrimonialInputData {
    private String preenchimento;
    private Long numeroInicio;
    private Long numeroFim;
    private Long quantidadeReservada;
}
