package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.listagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.listagem.BuscarIncorporacoesOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;

import java.util.Objects;
import java.util.stream.Collectors;

public class BuscarIncorporacoesOutputDataConverter {

    public BuscarIncorporacoesOutputData to(ListaPaginada<Incorporacao> from) {
        BuscarIncorporacoesOutputDataItemConverter outputDataItemConverter = new BuscarIncorporacoesOutputDataItemConverter();

        return BuscarIncorporacoesOutputData
            .builder()
            .totalElements(from.getTotalElements())
            .totalPages(from.getTotalPages())
            .items(from
                .getItems()
                .stream()
                .map(outputDataItemConverter::to)
                .collect(Collectors.toList()))
            .build();
    }

    private static class BuscarIncorporacoesOutputDataItemConverter extends GenericConverter<Incorporacao, BuscarIncorporacoesOutputData.Item> {

        @Override
        public BuscarIncorporacoesOutputData.Item to(Incorporacao source) {
            BuscarIncorporacoesOutputData.Item target = super.to(source);

            if (Objects.nonNull(source.getDataRecebimento())) {
                target.setDataRecebimento(DateValidate.formatarData(source.getDataRecebimento()));
            }
            if (Objects.nonNull(source.getFornecedor())) {
                target.setFornecedor(source.getFornecedor().getNome());
            }
            if (Objects.nonNull(source.getDataNota())) {
                target.setDataNota(DateValidate.formatarData(source.getDataNota()));
            }
            if (Objects.nonNull(source.getOrgao())) {
                target.setOrgao(source.getOrgao().getSigla());
            }
            if (Objects.nonNull(source.getSetor())) {
                target.setSetor(source.getSetor().getId());
            }
            if (Objects.nonNull(source.getReconhecimento())) {
                target.setReconhecimento(BuscarIncorporacoesOutputData.Item.Reconhecimento
                    .builder()
                    .id(source.getReconhecimento().getId())
                    .empenho(source.getReconhecimento().getEmpenho())
                    .notaFiscal(source.getReconhecimento().getNotaFiscal())
                    .build());
            }
            if (Objects.nonNull(source.getFundo())) {
                target.setFundo(source.getFundo().getId());
            }
            if (Objects.nonNull(source.getConvenio())) {
                target.setConvenio(source.getConvenio().getId());
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
}
