package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditarPatrimonioInputData {
    private Long id;
    private String uriImagem;
    private String placa;
    private String renavam;
    private String licenciamento;
    private String motor;
    private String chassi;
    private String numeroSerie;
}
