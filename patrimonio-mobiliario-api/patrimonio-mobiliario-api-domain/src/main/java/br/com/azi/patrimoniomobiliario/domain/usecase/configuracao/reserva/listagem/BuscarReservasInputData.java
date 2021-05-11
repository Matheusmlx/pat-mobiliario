package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.listagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuscarReservasInputData {
    private String conteudo;
    private Long page;
    private Long size;
    private String sort;
    private String direction;
}
