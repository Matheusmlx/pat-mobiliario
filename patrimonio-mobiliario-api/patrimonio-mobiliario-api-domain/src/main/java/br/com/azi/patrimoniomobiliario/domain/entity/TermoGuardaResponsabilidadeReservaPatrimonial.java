package br.com.azi.patrimoniomobiliario.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TermoGuardaResponsabilidadeReservaPatrimonial {
    private String orgao;
    private String codigo;
    private String numeroTermo;
    private String dataCriacao;
    private String preenchimento;
    private Long quantidade;
    private String intervalo;
}
