package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.listagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.listagem.BuscarMovimentacoesInternasOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;

import java.util.Objects;
import java.util.stream.Collectors;

public class BuscarMovimentacoesInternasOutputDataConverter {

    public BuscarMovimentacoesInternasOutputData to(ListaPaginada<Movimentacao> movimentacoes) {
        BuscarMovimentacaoInternaOutputDataConverter converter = new BuscarMovimentacaoInternaOutputDataConverter();

        return BuscarMovimentacoesInternasOutputData
            .builder()
            .items(movimentacoes
                .getItems()
                .stream()
                .map(converter::to)
                .collect(Collectors.toList()))
            .totalElements(movimentacoes.getTotalElements())
            .totalPages(movimentacoes.getTotalPages())
            .build();
    }

    public static class BuscarMovimentacaoInternaOutputDataConverter extends GenericConverter<Movimentacao, BuscarMovimentacoesInternasOutputData.Item> {

        @Override
        public BuscarMovimentacoesInternasOutputData.Item to(Movimentacao source) {
            BuscarMovimentacoesInternasOutputData.Item target = super.to(source);

            if (Objects.nonNull(source.getOrgaoOrigem())) {
                target.setOrgao(source.getOrgaoOrigem().getSigla());
            }

            if (Objects.nonNull(source.getSetorOrigem())) {
                target.setSetorOrigem(String.format("%s - %s", source.getSetorOrigem().getSigla(), source.getSetorOrigem().getNome()));
            }

            if (Objects.nonNull(source.getSetorDestino())) {
                target.setSetorDestino(String.format("%s - %s", source.getSetorDestino().getSigla(), source.getSetorDestino().getNome()));
            }

            if (Objects.nonNull(source.getNotaLancamentoContabil()) &&
                Objects.nonNull(source.getNotaLancamentoContabil().getNumero())) {
                target.setNumeroNotaLancamentoContabil(source.getNotaLancamentoContabil().getNumero());
            }

            if (Objects.nonNull(source.getNotaLancamentoContabil()) &&
                Objects.nonNull(source.getNotaLancamentoContabil().getDataLancamento())) {
                target.setDataNotaLancamentoContabil(DateValidate.formatarData(source.getNotaLancamentoContabil().getDataLancamento()));
            }

            if (Objects.nonNull(source.getDataDevolucao())) {
                target.setDataDevolucao(DateValidate.formatarData(source.getDataDevolucao()));
            }

            if (Objects.nonNull(source.getNumeroProcesso())) {
                target.setNumeroProcesso(source.getNumeroProcesso());
            }

            if (Objects.nonNull(source.getResponsavel())) {
                target.setResponsavel(source.getResponsavel().getId());
            }

            return target;
        }

    }

}
