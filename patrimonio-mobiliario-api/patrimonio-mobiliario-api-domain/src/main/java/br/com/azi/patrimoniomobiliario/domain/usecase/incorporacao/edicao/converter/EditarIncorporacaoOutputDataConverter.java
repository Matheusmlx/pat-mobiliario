package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.edicao.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.edicao.EditarIncorporacaoOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;

import java.util.Objects;

public class EditarIncorporacaoOutputDataConverter extends GenericConverter<Incorporacao, EditarIncorporacaoOutputData> {

    @Override
    public EditarIncorporacaoOutputData to(Incorporacao source) {
        EditarIncorporacaoOutputData target = super.to(source);

        if (Objects.nonNull(source.getOrgao())) {
            target.setOrgao(
                EditarIncorporacaoOutputData.Orgao
                    .builder()
                    .id(source.getOrgao().getId())
                    .descricao(source.getOrgao().getSigla() + " - " + source.getOrgao().getNome())
                    .situacao(EditarIncorporacaoOutputData.Situacao.valueOf(source.getOrgao().getSituacao().name()))
                    .build());
        }
        if (Objects.nonNull(source.getSetor())) {
            target.setSetor(
                EditarIncorporacaoOutputData.Setor
                    .builder()
                    .id(source.getSetor().getId())
                    .descricao(source.getSetor().getSigla() + " - " + source.getSetor().getNome())
                    .situacao(EditarIncorporacaoOutputData.Situacao.valueOf(source.getSetor().getSituacao().name()))
                    .build());
        }
        if (Objects.nonNull(source.getConvenio())) {
            target.setConvenio(source.getConvenio().getId());
        }
        if (Objects.nonNull(source.getReconhecimento())) {
            target.setReconhecimento(
                EditarIncorporacaoOutputData.Reconhecimento
                    .builder()
                    .id(source.getReconhecimento().getId())
                    .nome(source.getReconhecimento().getNome())
                    .situacao(EditarIncorporacaoOutputData.Situacao.valueOf(source.getReconhecimento().getSituacao().name()))
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
        if (Objects.nonNull(source.getFornecedor())) {
            target.setFornecedor(
                EditarIncorporacaoOutputData.Fornecedor.builder()
                    .id(source.getFornecedor().getId())
                    .cpfCnpj(source.getFornecedor().getCpfCnpj())
                    .nome(source.getFornecedor().getNome())
                    .situacao(EditarIncorporacaoOutputData.Situacao.valueOf(source.getFornecedor().getSituacao().name()))
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
        if (Objects.nonNull(source.getDataNota())) {
            target.setDataNota(DateValidate.formatarData(source.getDataNota()));
        }
        if (Objects.nonNull(source.getDataRecebimento())) {
            target.setDataRecebimento(DateValidate.formatarData(source.getDataRecebimento()));
        }
        if (Objects.nonNull(source.getComodante())) {
            target.setComodante(
                EditarIncorporacaoOutputData.Comodante
                    .builder()
                    .id(source.getComodante().getId())
                    .nome(source.getComodante().getNome())
                    .build());
        }
        return target;
    }
}
