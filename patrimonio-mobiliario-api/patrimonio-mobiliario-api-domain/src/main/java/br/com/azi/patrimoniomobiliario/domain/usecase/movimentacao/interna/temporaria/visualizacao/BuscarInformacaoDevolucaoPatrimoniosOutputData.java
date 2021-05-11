package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.visualizacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarInformacaoDevolucaoPatrimoniosOutputData {
    private String razaoPatrimoniosDevolvidos;
}
