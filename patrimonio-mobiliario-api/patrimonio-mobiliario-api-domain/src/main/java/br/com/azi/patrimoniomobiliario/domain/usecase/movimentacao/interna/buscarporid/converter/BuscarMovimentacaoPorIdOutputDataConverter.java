package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarporid.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.buscarporid.BuscarMovimentacaoPorIdOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;

import java.util.Objects;

public class BuscarMovimentacaoPorIdOutputDataConverter extends GenericConverter<Movimentacao, BuscarMovimentacaoPorIdOutputData> {

    @Override
    public BuscarMovimentacaoPorIdOutputData to(Movimentacao source) {
        final BuscarMovimentacaoPorIdOutputData target = super.to(source);
        target.setSituacao(BuscarMovimentacaoPorIdOutputData.Situacao.valueOf(source.getSituacao().name()));

        if (Objects.nonNull(source.getOrgaoOrigem())) {
            target.setOrgao(source.getOrgaoOrigem().getId());
        }

        if (Objects.nonNull(source.getSetorOrigem())) {
            target.setSetorOrigem(source.getSetorOrigem().getId());
        }

        if (Objects.nonNull(source.getSetorDestino())) {
            target.setSetorDestino(source.getSetorDestino().getId());
        }

        if (Objects.nonNull(source.getNotaLancamentoContabil())) {
            target.setNumeroNotaLancamentoContabil(source.getNotaLancamentoContabil().getNumero());
            if (Objects.nonNull(source.getNotaLancamentoContabil().getDataLancamento())) {
                target.setDataNotaLancamentoContabil(DateValidate.formatarData(source.getNotaLancamentoContabil().getDataLancamento()));
            }
        }

        if(Objects.nonNull(source.getDataDevolucao())){
            target.setDataDevolucao(DateValidate.formatarData(source.getDataDevolucao()));
        }

        if (Objects.nonNull(source.getResponsavel())) {
            target.setResponsavel(source.getResponsavel().getId());
        }

        return target;
    }
}
