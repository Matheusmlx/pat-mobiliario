package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.cadastro.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.cadastro.CadastrarMovimentacaoInternaOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.Objects;

public class CadastrarMovimentacaoInternaOutputDataConverter extends GenericConverter<Movimentacao, CadastrarMovimentacaoInternaOutputData> {

    @Override
    public CadastrarMovimentacaoInternaOutputData to(Movimentacao source) {
        CadastrarMovimentacaoInternaOutputData target = super.to(source);

        if (Objects.nonNull(source.getTipo())) {
            target.setTipo(source.getTipo().toString());
        }

        if (Objects.nonNull(source.getSituacao())) {
            target.setSituacao(source.getSituacao().toString());
        }

        if (Objects.nonNull(source.getUsuarioCriacao())) {
            target.setUsuarioCriacao(source.getUsuarioCriacao());
        }

        return target;
    }

}
