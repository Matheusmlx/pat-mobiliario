package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.edicao.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entreestoques.edicao.EditarMovimentacaoEntreEstoquesOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;

import java.util.Objects;

public class EditarMovimentacaoEntreEstoquesOutputDataConverter extends GenericConverter<Movimentacao, EditarMovimentacaoEntreEstoquesOutputData> {

    @Override
    public EditarMovimentacaoEntreEstoquesOutputData to(Movimentacao source) {
        final EditarMovimentacaoEntreEstoquesOutputData target = super.to(source);

        if (Objects.nonNull(source.getOrgaoOrigem())) {
            target.setOrgao(source.getOrgaoOrigem().getId());
        }

        if (Objects.nonNull(source.getSetorOrigem())) {
            target.setSetorOrigem(source.getSetorOrigem().getId());
        }

        if (Objects.nonNull(source.getSetorDestino())) {
            target.setSetorDestino(source.getSetorDestino().getId());
        }

        if (Objects.nonNull(source.getResponsavel())) {
            target.setResponsavel(source.getResponsavel().getId());
        }

        if (Objects.nonNull(source.getNotaLancamentoContabil())) {
            target.setNumeroNotaLancamentoContabil(source.getNotaLancamentoContabil().getNumero());
            if (Objects.nonNull(source.getNotaLancamentoContabil().getDataLancamento())) {
                target.setDataNotaLancamentoContabil(DateValidate.formatarData(source.getNotaLancamentoContabil().getDataLancamento()));
            }
        }

        return target;
    }
}
