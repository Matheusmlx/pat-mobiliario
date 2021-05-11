package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.visualizacao.converter;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.visualizacao.BuscarInformacaoDevolucaoPatrimoniosOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.Objects;

public class BuscarInformacaoDevolucaoPatrimoniosOutputDataConverter extends GenericConverter<String, BuscarInformacaoDevolucaoPatrimoniosOutputData> {

    @Override
    public BuscarInformacaoDevolucaoPatrimoniosOutputData to(String source) {
        BuscarInformacaoDevolucaoPatrimoniosOutputData target = super.to(source);

        if (Objects.nonNull(source)) {
            target.setRazaoPatrimoniosDevolvidos(source);
        }

        return target;
    }

}
