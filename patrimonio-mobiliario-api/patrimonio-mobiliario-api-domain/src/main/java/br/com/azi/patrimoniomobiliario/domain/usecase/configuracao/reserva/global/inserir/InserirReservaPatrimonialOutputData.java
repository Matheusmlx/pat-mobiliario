package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InserirReservaPatrimonialOutputData {
    private Long id;
    private String codigo;
    private String situacao;
    private String preenchimento;
    private Long quantidadeReservada;
    private Long quantidadeRestante;
    private Long numeroInicio;
    private Long numeroFim;
}
