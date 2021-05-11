package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.visualizar.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.visualizar.VisualizarIncorporacaoOutputData;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;

import java.util.Objects;

public class VisualizarIncorporacaoOutputDataConverter {

    public VisualizarIncorporacaoOutputData to(Incorporacao source, boolean possuiPatrimoniosEmOutrasMovimentacoes) {
        VisualizarIncorporacaoOutputData target = new VisualizarIncorporacaoOutputData();
        target.setId(source.getId());
        target.setCodigo(source.getCodigo());
        target.setSituacao(source.getSituacao().name());
        target.setNumProcesso(source.getNumProcesso());
        target.setNota(source.getNota());
        target.setValorNota(source.getValorNota());
        target.setProjeto(source.getProjeto());
        target.setPossuiPatrimoniosEmOutrasMovimentacoes(possuiPatrimoniosEmOutrasMovimentacoes);

        if (Objects.nonNull(source.getOrgao())) {
            target.setOrgao(source.getOrgao().getSigla() + " - " + source.getOrgao().getNome());
        }
        if (Objects.nonNull(source.getSetor())) {
            target.setSetor(source.getSetor().getSigla() + " - " + source.getSetor().getNome());
        }
        if (Objects.nonNull(source.getConvenio())) {
            target.setConvenio(source.getConvenio().getNome());
        }
        if (Objects.nonNull(source.getReconhecimento())) {
            target.setReconhecimento(source.getReconhecimento().getNome());
        }
        if (Objects.nonNull(source.getDataRecebimento())) {
            target.setDataRecebimento(DateValidate.formatarData(source.getDataRecebimento()));
        }
        if (Objects.nonNull(source.getDataNota())) {
            target.setDataNota(DateValidate.formatarData(source.getDataNota()));
        }
        if (Objects.nonNull(source.getDataFinalizacao())) {
            target.setDataFinalizacao(DateValidate.formatarData(source.getDataFinalizacao()));
        }
        if (Objects.nonNull(source.getFornecedor())) {
            target.setFornecedor(source.getFornecedor().getNome());
        }
        if (Objects.nonNull(source.getFundo())) {
            target.setFundo(source.getFundo().getSigla() + " - " + source.getFundo().getNome());
        }
        if (Objects.nonNull(source.getNotaLancamentoContabil())) {
            target.setNumeroNotaLancamentoContabil(source.getNotaLancamentoContabil().getNumero());
            if (Objects.nonNull(source.getNotaLancamentoContabil().getDataLancamento())) {
                target.setDataNotaLancamentoContabil(DateValidate.formatarData(source.getNotaLancamentoContabil().getDataLancamento()));
            }
        }
        if (Objects.nonNull(source.getComodante()) && Objects.nonNull(source.getComodante().getNome())) {
            target.setComodante(source.getComodante().getNome());
        }

        return target;
    }
}
