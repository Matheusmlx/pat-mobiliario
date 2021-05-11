package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.edicao.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.entresetores.edicao.EditarMovimentacaoEntreSetoresOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;

import java.util.Objects;

public class EditarMovimentacaoEntreSetoresOutputDataConverter extends GenericConverter<Movimentacao, EditarMovimentacaoEntreSetoresOutputData> {

    @Override
    public EditarMovimentacaoEntreSetoresOutputData to(Movimentacao source) {
        final EditarMovimentacaoEntreSetoresOutputData target = super.to(source);
        target.setSituacao(EditarMovimentacaoEntreSetoresOutputData.Situacao.valueOf(source.getSituacao().name()));

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
