package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.distribuicao.finalizacao.finalizar.FinalizarDistribuicaoOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;

import java.util.Objects;

public class FinalizarDistribuicaoOutputDataConverter extends GenericConverter<Movimentacao, FinalizarDistribuicaoOutputData> {

    @Override
    public FinalizarDistribuicaoOutputData to(Movimentacao source) {
        FinalizarDistribuicaoOutputData target = super.to(source);

        if (Objects.nonNull(source.getDataFinalizacao())) {
            target.setDataFinalizacao(DateValidate.formatarData(source.getDataFinalizacao()));
        }

        return target;
    }

}
