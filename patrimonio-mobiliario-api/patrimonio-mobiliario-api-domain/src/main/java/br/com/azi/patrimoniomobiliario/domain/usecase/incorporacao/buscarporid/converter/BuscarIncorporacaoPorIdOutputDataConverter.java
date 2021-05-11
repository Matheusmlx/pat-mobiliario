package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarporid.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.buscarporid.BuscarIncorporacaoPorIdOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;

import java.util.Objects;

public class BuscarIncorporacaoPorIdOutputDataConverter extends GenericConverter<Incorporacao, BuscarIncorporacaoPorIdOutputData> {

    @Override
    public BuscarIncorporacaoPorIdOutputData to(Incorporacao source) {
        BuscarIncorporacaoPorIdOutputData target = super.to(source);

        if (Objects.nonNull(source.getOrgao())) {
            target.setOrgao(
                BuscarIncorporacaoPorIdOutputData.Orgao
                    .builder()
                    .id(source.getOrgao().getId())
                    .descricao(source.getOrgao().getSigla() + " - " + source.getOrgao().getNome())
                    .situacao(BuscarIncorporacaoPorIdOutputData.Situacao.valueOf(source.getOrgao().getSituacao().name()))
                    .build());
        }
        if (Objects.nonNull(source.getSetor())) {
            target.setSetor(
                BuscarIncorporacaoPorIdOutputData.Setor
                    .builder()
                    .id(source.getSetor().getId())
                    .descricao(source.getSetor().getSigla() + " - " + source.getSetor().getNome())
                    .situacao(BuscarIncorporacaoPorIdOutputData.Situacao.valueOf(source.getSetor().getSituacao().name()))
                    .build());
        }
        if (Objects.nonNull(source.getConvenio())) {
            target.setConvenio(source.getConvenio().getId());
        }
        if (Objects.nonNull(source.getReconhecimento())) {
            target.setReconhecimento(BuscarIncorporacaoPorIdOutputData.Reconhecimento
                .builder()
                .id(source.getReconhecimento().getId())
                .nome(source.getReconhecimento().getNome())
                .situacao(BuscarIncorporacaoPorIdOutputData.Situacao.valueOf(source.getReconhecimento().getSituacao().name()))
                .execucaoOrcamentaria(source.getReconhecimento().getExecucaoOrcamentaria())
                .empenho(source.getReconhecimento().getEmpenho())
                .notaFiscal(source.getReconhecimento().getNotaFiscal())
                .build());
        }
        if (Objects.nonNull(source.getEmpenho())) {
            target.setEmpenho(source.getEmpenho().getId());
        }
        if (Objects.nonNull(source.getNaturezaDespesa())) {
            target.setNaturezaDespesa(source.getNaturezaDespesa().getId());
        }
        if (Objects.nonNull(source.getDataRecebimento())) {
            target.setDataRecebimento(DateValidate.formatarData(source.getDataRecebimento()));
        }
        if (Objects.nonNull(source.getDataNota())) {
            target.setDataNota(DateValidate.formatarData(source.getDataNota()));
        }
        if (Objects.nonNull(source.getFornecedor())) {
            target.setFornecedor(
                BuscarIncorporacaoPorIdOutputData.Fornecedor.builder()
                    .id(source.getFornecedor().getId())
                    .cpfCnpj(source.getFornecedor().getCpfCnpj())
                    .nome(source.getFornecedor().getNome())
                    .situacao(BuscarIncorporacaoPorIdOutputData.Situacao.valueOf(source.getFornecedor().getSituacao().name()))
                    .build());
        }
        if (Objects.nonNull(source.getFundo())) {
            target.setFundo(source.getFundo().getId());
        }
        if (Objects.nonNull(source.getNotaLancamentoContabil())) {
            target.setNumeroNotaLancamentoContabil(source.getNotaLancamentoContabil().getNumero());
            if (Objects.nonNull(source.getNotaLancamentoContabil().getDataLancamento())) {
                target.setDataNotaLancamentoContabil(DateValidate.formatarData(source.getNotaLancamentoContabil().getDataLancamento()));
            }
        }
        if (Objects.nonNull(source.getComodante())) {
            target.setComodante(
                BuscarIncorporacaoPorIdOutputData.Comodante
                    .builder()
                    .id(source.getComodante().getId())
                    .nome(source.getComodante().getNome())
                    .build());
        }

        return target;
    }
}
