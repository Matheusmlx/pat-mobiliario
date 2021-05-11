package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import br.com.azi.patrimoniomobiliario.domain.constant.movimentacao.TipoMovimentacaoEnum;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.MovimentacaoItem;
import br.com.azi.patrimoniomobiliario.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.MovimentacaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.MovimentacaoItemEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.MovimentacaoItemPK;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.PatrimonioEntity;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import br.com.azi.patrimoniomobiliario.utils.string.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class MovimentacaoItemConverter extends GenericConverter<MovimentacaoItemEntity, MovimentacaoItem> {

    @Autowired
    PatrimonioConverter patrimonioConverter;

    @Override
    public MovimentacaoItemEntity from(MovimentacaoItem source) {
        MovimentacaoItemEntity target = super.from(source);

        if (Objects.nonNull(source.getMovimentacao()) && Objects.nonNull(source.getPatrimonio())) {
            target.setMovimentacaoItemPK(MovimentacaoItemPK
                .builder()
                .movimentacao(MovimentacaoEntity.builder()
                    .id(source.getMovimentacao().getId())
                    .build())
                .patrimonio(PatrimonioEntity.builder()
                    .id(source.getPatrimonio().getId())
                    .build())
                .build());
        }

        if (Objects.nonNull(source.getDataDevolucao())) {
            target.setDataDevolucao(DateUtils.asDate(source.getDataDevolucao()));
        }
        return target;
    }

    @Override
    public MovimentacaoItem to(MovimentacaoItemEntity source) {
        MovimentacaoItem target = super.to(source);

        if (Objects.nonNull(source.getMovimentacaoItemPK().getMovimentacao())) {
            Movimentacao movimentacao = Movimentacao.builder()
                .id(source.getMovimentacaoItemPK().getMovimentacao().getId())
                .build();

            if (StringUtils.isNotEmpty(source.getMovimentacaoItemPK().getMovimentacao().getTipo())) {
                movimentacao.setTipo(TipoMovimentacaoEnum.valueOf(source.getMovimentacaoItemPK().getMovimentacao().getTipo()));
            }

            if (Objects.nonNull(source.getMovimentacaoItemPK().getMovimentacao().getDataCadastro())) {
                movimentacao.setDataCadastro(source.getMovimentacaoItemPK().getMovimentacao().getDataCadastro());
            }

            if (Objects.nonNull(source.getMovimentacaoItemPK().getMovimentacao().getDataFinalizacao())) {
                movimentacao.setDataFinalizacao(DateUtils.asLocalDateTime(source.getMovimentacaoItemPK().getMovimentacao().getDataFinalizacao()));
            }

            if (Objects.nonNull(source.getMovimentacaoItemPK().getMovimentacao().getMotivoObservacao())) {
                movimentacao.setMotivoObservacao(source.getMovimentacaoItemPK().getMovimentacao().getMotivoObservacao());
            }

            if (Objects.nonNull(source.getMovimentacaoItemPK().getMovimentacao().getNotaLancamentoContabil())) {
                movimentacao.setNotaLancamentoContabil(
                    NotaLancamentoContabil
                        .builder()
                        .numero(source.getMovimentacaoItemPK().getMovimentacao().getNotaLancamentoContabil().getNumero())
                        .dataLancamento(
                            DateUtils.asLocalDateTime(source.getMovimentacaoItemPK().getMovimentacao().getNotaLancamentoContabil().getDataLancamento()))
                        .build()
                );
            }

            if (Objects.nonNull(source.getMovimentacaoItemPK().getMovimentacao().getOrgaoOrigem())) {
                movimentacao.setOrgaoOrigem(
                    UnidadeOrganizacional
                        .builder()
                        .id(source.getMovimentacaoItemPK().getMovimentacao().getOrgaoOrigem().getId())
                        .nome(source.getMovimentacaoItemPK().getMovimentacao().getOrgaoOrigem().getNome())
                        .sigla(source.getMovimentacaoItemPK().getMovimentacao().getOrgaoOrigem().getSigla())
                        .build());
            }

            if (Objects.nonNull(source.getMovimentacaoItemPK().getMovimentacao().getSetorOrigem())) {
                movimentacao.setSetorOrigem(
                    UnidadeOrganizacional
                        .builder()
                        .id(source.getMovimentacaoItemPK().getMovimentacao().getSetorOrigem().getId())
                        .descricao(
                            String.format("%s - %s", source.getMovimentacaoItemPK().getMovimentacao().getSetorOrigem().getSigla(),
                                source.getMovimentacaoItemPK().getMovimentacao().getSetorOrigem().getNome()))
                        .build());
            }

            if (Objects.nonNull(source.getMovimentacaoItemPK().getMovimentacao().getOrgaoDestino())) {
                movimentacao.setOrgaoDestino(
                    UnidadeOrganizacional
                        .builder()
                        .id(source.getMovimentacaoItemPK().getMovimentacao().getOrgaoDestino().getId())
                        .nome(source.getMovimentacaoItemPK().getMovimentacao().getOrgaoDestino().getNome())
                        .sigla(source.getMovimentacaoItemPK().getMovimentacao().getOrgaoDestino().getSigla())
                        .build());
            }

            if (Objects.nonNull(source.getMovimentacaoItemPK().getMovimentacao().getSetorDestino())) {
                movimentacao.setSetorDestino(
                    UnidadeOrganizacional
                        .builder()
                        .id(source.getMovimentacaoItemPK().getMovimentacao().getSetorDestino().getId())
                        .descricao(
                            String.format("%s - %s", source.getMovimentacaoItemPK().getMovimentacao().getSetorDestino().getSigla(),
                                source.getMovimentacaoItemPK().getMovimentacao().getSetorDestino().getNome()))
                        .build()
                );
            }

            if (StringUtils.isNotEmpty(source.getMovimentacaoItemPK().getMovimentacao().getSituacao())) {
                movimentacao.setSituacao(Movimentacao.Situacao.valueOf(source.getMovimentacaoItemPK().getMovimentacao().getSituacao()));
            }

            target.setMovimentacao(movimentacao);
        }

        if (Objects.nonNull(source.getMovimentacaoItemPK().getPatrimonio())) {
            target.setPatrimonio(patrimonioConverter.to(source.getMovimentacaoItemPK().getPatrimonio()));
        }

        if (Objects.nonNull(source.getDataDevolucao())) {
            target.setDataDevolucao(DateUtils.asLocalDateTime(source.getDataDevolucao()));
        }

        return target;
    }
}
