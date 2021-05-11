package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.buscarporpatrimonio.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.buscarporpatrimonio.BuscarMovimentacoesPorPatrimonioOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BuscarMovimentacoesPorPatrimonioOutputDataConverter {

    public BuscarMovimentacoesPorPatrimonioOutputData to(List<MovimentacaoItem> movimentacoes) {
        MovimentacaoOutputDataConverter outputDataConverter = new MovimentacaoOutputDataConverter();

        return BuscarMovimentacoesPorPatrimonioOutputData.
            builder()
            .items(movimentacoes
                .stream()
                .map(outputDataConverter::to)
                .collect(Collectors.toList()))
            .build();
    }

    public static class MovimentacaoOutputDataConverter extends GenericConverter<MovimentacaoItem, BuscarMovimentacoesPorPatrimonioOutputData.Item> {

        @Override
        public BuscarMovimentacoesPorPatrimonioOutputData.Item to(MovimentacaoItem source) {
            BuscarMovimentacoesPorPatrimonioOutputData.Item target = super.to(source);

            if (Objects.nonNull(source.getMovimentacao().getId())) {
                target.setId(source.getMovimentacao().getId());
            }

            if (Objects.nonNull(source.getMovimentacao().getTipo())) {
                target.setTipo(source.getMovimentacao().getTipo().toString());
            }

            if (Objects.nonNull(source.getMovimentacao().getDataFinalizacao())) {
                target.setDataFinalizacao(DateValidate.formatarData(source.getMovimentacao().getDataFinalizacao()));
            }

            if (Objects.nonNull(source.getMovimentacao().getDataCadastro())) {
                target.setDataCriacao(DateValidate.formatarData(source.getMovimentacao().getDataCadastro()));
            }

            if (Objects.nonNull(source.getPatrimonio().getValorAquisicao())) {
                target.setValorTotal(source.getPatrimonio().getValorAquisicao());
            }

            if (Objects.nonNull(source.getMovimentacao().getSituacao())) {
                target.setSituacao(source.getMovimentacao().getSituacao().toString());
            }

            if (Objects.nonNull(source.getMovimentacao().getOrgaoOrigem())) {
                target.setOrgao(source.getMovimentacao().getOrgaoOrigem().getSigla());
            }

            if (Objects.nonNull(source.getMovimentacao().getSetorOrigem())) {
                target.setSetorOrigem(source.getMovimentacao().getSetorOrigem().getDescricao());
            }

            if (Objects.nonNull(source.getMovimentacao().getSetorDestino())) {
                target.setSetorDestino(source.getMovimentacao().getSetorDestino().getDescricao());
            }

            if (Objects.nonNull(source.getMovimentacao().getMotivoObservacao())) {
                target.setMotivoObservacao(source.getMovimentacao().getMotivoObservacao());
            }

            if (Objects.nonNull(source.getMovimentacao().getNotaLancamentoContabil()) &&
                Objects.nonNull(source.getMovimentacao().getNotaLancamentoContabil().getNumero())) {
                target.setNumeroNotaLancamentoContabil(source.getMovimentacao().getNotaLancamentoContabil().getNumero());
            }

            if (Objects.nonNull(source.getMovimentacao().getNotaLancamentoContabil()) &&
                Objects.nonNull(source.getMovimentacao().getNotaLancamentoContabil().getDataLancamento())) {
                target.setDataNotaLancamentoContabil(DateValidate.formatarData(source.getMovimentacao().getNotaLancamentoContabil().getDataLancamento()));
            }

            if (Objects.nonNull(source.getMovimentacao().getNumeroProcesso())) {
                target.setNumeroProcesso(source.getMovimentacao().getNumeroProcesso());
            }

            return target;
        }

    }

}
