package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemincorporados;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarPatrimoniosIncorporadosInputData {
    private Long page;
    private Long size;
    private String sort;
    private String direction;
    private String conteudo;
}
