package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.inserir;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InserirReservaPorOrgaoInputData {
    private String preenchimento;
    private Long orgaoId;
    private Long numeroInicio;
    private Long numeroFim;
    private Long quantidadeReservada;
}
