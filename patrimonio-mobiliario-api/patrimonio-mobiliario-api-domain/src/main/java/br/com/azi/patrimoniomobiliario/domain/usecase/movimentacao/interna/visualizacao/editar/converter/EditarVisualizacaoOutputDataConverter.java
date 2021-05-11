package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.editar.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.visualizacao.editar.EditarVisualizacaoOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;

import java.util.Objects;

public class EditarVisualizacaoOutputDataConverter extends GenericConverter<Movimentacao, EditarVisualizacaoOutputData> {

    @Override
    public EditarVisualizacaoOutputData to(Movimentacao source) {
        EditarVisualizacaoOutputData target = super.to(source);

        if (Objects.nonNull(source.getSetorOrigem())) {
            target.setSetorOrigem(
                source.getSetorOrigem().getSigla() + " - " + source.getSetorOrigem().getNome()
            );
        }

        if (Objects.nonNull(source.getOrgaoOrigem())) {
            target.setOrgao(source.getOrgaoOrigem().getSigla() + " - " + source.getOrgaoOrigem().getNome());
        }

        if (Objects.nonNull(source.getNotaLancamentoContabil())) {
            target.setNumeroNotaLancamentoContabil(source.getNotaLancamentoContabil().getNumero());
            if (Objects.nonNull(source.getNotaLancamentoContabil().getDataLancamento())) {
                target.setDataNotaLancamentoContabil(DateValidate.formatarData(source.getNotaLancamentoContabil().getDataLancamento()));
            }
        }

        if (Objects.nonNull(source.getSetorDestino())) {
            target.setSetorDestino(
                source.getSetorDestino().getSigla() + " - " + source.getSetorDestino().getNome()
            );
        }

        if (Objects.nonNull(source.getTipo())) {
            target.setTipo(source.getTipo().name());
        }

        if (Objects.nonNull(source.getDataFinalizacao())) {
            target.setDataFinalizacao(DateValidate.formatarData(source.getDataFinalizacao()));
        }

        if (Objects.nonNull(source.getDataDevolucao())) {
            target.setDataDevolucao(DateValidate.formatarData(source.getDataDevolucao()));
        }

        if (Objects.nonNull(source.getDataEnvio())) {
            target.setDataEnvio(DateValidate.formatarData(source.getDataEnvio()));
        }

        return target;
    }
}
