package br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Depreciacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.calculardepreciacao.CalcularDepreciacaoOutputData;

import java.util.List;

public class CalcularDepreciacaoOutputDataConverter  {

    public CalcularDepreciacaoOutputData to(Patrimonio patrimonio, List<Depreciacao> depreciacoes) {

        CalcularDepreciacaoOutputData calcularDepreciacaoOutputData = new CalcularDepreciacaoOutputData();

        calcularDepreciacaoOutputData.setPatrimonio(patrimonio);
        calcularDepreciacaoOutputData.setDepreciacao(depreciacoes);
        return calcularDepreciacaoOutputData;
    }
}
