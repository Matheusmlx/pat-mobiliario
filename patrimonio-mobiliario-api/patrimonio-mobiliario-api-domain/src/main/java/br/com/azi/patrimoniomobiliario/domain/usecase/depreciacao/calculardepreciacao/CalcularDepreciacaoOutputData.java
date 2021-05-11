package br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao;

import br.com.azi.patrimoniomobiliario.domain.entity.Depreciacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CalcularDepreciacaoOutputData {
    private Patrimonio patrimonio;
    private List<Depreciacao> depreciacao;
}
