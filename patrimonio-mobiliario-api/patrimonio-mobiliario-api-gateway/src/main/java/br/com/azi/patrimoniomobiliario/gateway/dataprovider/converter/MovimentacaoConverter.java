package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Responsavel;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.IncorporacaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.MovimentacaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ResponsavelEntity;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class MovimentacaoConverter extends GenericConverter<MovimentacaoEntity, Movimentacao> {

    @Autowired
    private NotaLancamentoContabilConverter notaLancamentoContabilConverter;

    @Autowired
    private UnidadeOrganizacionalConverter unidadeOrganizacionalConverter;

    @Override
    public Movimentacao to(MovimentacaoEntity source) {
        Movimentacao target = super.to(source);

        if (Objects.nonNull(source.getDataFinalizacao())) {
            target.setDataFinalizacao(DateUtils.asLocalDateTime(source.getDataFinalizacao()));
        }

        if (Objects.nonNull(source.getOrgaoOrigem())) {
            try {
                target.setOrgaoOrigem(unidadeOrganizacionalConverter.to(source.getOrgaoOrigem()));
            } catch (Exception e) {
                target.setOrgaoOrigem(UnidadeOrganizacional.builder()
                    .id(source.getOrgaoOrigem().getId())
                    .build());
            }
        }

        if (Objects.nonNull(source.getOrgaoDestino())) {
            try {
                target.setOrgaoDestino(unidadeOrganizacionalConverter.to(source.getOrgaoDestino()));
            } catch (Exception e) {
                target.setOrgaoDestino(UnidadeOrganizacional.builder()
                    .id(source.getOrgaoDestino().getId())
                    .build());
            }
        }

        if (Objects.nonNull(source.getSetorOrigem())) {
            try {
                target.setSetorOrigem(unidadeOrganizacionalConverter.to(source.getSetorOrigem()));
            } catch (Exception e) {
                target.setSetorOrigem(UnidadeOrganizacional.builder()
                    .id(source.getSetorOrigem().getId())
                    .build());
            }
        }

        if (Objects.nonNull(source.getSetorDestino())) {
            try {
                target.setSetorDestino(unidadeOrganizacionalConverter.to(source.getSetorDestino()));
            } catch (Exception e) {
                target.setSetorDestino(UnidadeOrganizacional.builder()
                    .id(source.getSetorDestino().getId())
                    .build());
            }
        }

        if (Objects.nonNull(source.getNotaLancamentoContabil())) {
            try {
                target.setNotaLancamentoContabil(notaLancamentoContabilConverter.to(source.getNotaLancamentoContabil()));
            } catch (Exception e) {
                target.setNotaLancamentoContabil(NotaLancamentoContabil.builder()
                    .id(source.getNotaLancamentoContabil().getId())
                    .build());
            }
        }

        if (Objects.nonNull(source.getResponsavel()) && Objects.nonNull(source.getResponsavel().getId())) {
            try {
                target.setResponsavel(
                    Responsavel
                        .builder()
                        .id(source.getResponsavel().getId())
                        .nome(source.getResponsavel().getNome())
                        .build()
                );
            } catch (Exception e) {
                target.setResponsavel(Responsavel.builder()
                    .id(source.getResponsavel().getId())
                    .build());
            }
        }

        if (Objects.nonNull(source.getDataDevolucao())) {
            target.setDataDevolucao(DateUtils.asLocalDateTime(source.getDataDevolucao()));
        }

        if (Objects.nonNull(source.getDataEnvio())) {
            target.setDataEnvio(DateUtils.asLocalDateTime(source.getDataEnvio()));
        }

        if (Objects.nonNull(source.getDataInicioProcessamento())) {
            target.setDataInicioProcessamento(DateUtils.asLocalDateTime(source.getDataInicioProcessamento()));
        }

        if (Objects.nonNull(source.getDataFimProcessamento())) {
            target.setDataFimProcessamento(DateUtils.asLocalDateTime(source.getDataFimProcessamento()));
        }

        return target;
    }

    @Override
    public MovimentacaoEntity from(Movimentacao source) {
        MovimentacaoEntity target = super.from(source);

        if (Objects.nonNull(source.getDataFinalizacao())) {
            target.setDataFinalizacao(DateUtils.asDate(source.getDataFinalizacao()));
        }

        if (Objects.nonNull(source.getOrgaoOrigem())) {
            target.setOrgaoOrigem(unidadeOrganizacionalConverter.from(source.getOrgaoOrigem()));
        }

        if (Objects.nonNull(source.getOrgaoDestino())) {
            target.setOrgaoDestino(unidadeOrganizacionalConverter.from(source.getOrgaoDestino()));
        }

        if (Objects.nonNull(source.getSetorOrigem())) {
            target.setSetorOrigem(unidadeOrganizacionalConverter.from(source.getSetorOrigem()));
        }

        if (Objects.nonNull(source.getSetorDestino())) {
            target.setSetorDestino(unidadeOrganizacionalConverter.from(source.getSetorDestino()));
        }

        if (Objects.nonNull(source.getNotaLancamentoContabil())) {
            target.setNotaLancamentoContabil(notaLancamentoContabilConverter.from(source.getNotaLancamentoContabil()));
        }

        if (Objects.nonNull(source.getResponsavel()) &&
            Objects.nonNull(source.getResponsavel().getId())) {
            target.setResponsavel(
                ResponsavelEntity
                    .builder()
                    .id(source.getResponsavel().getId())
                    .build()
            );
        }

        if (Objects.nonNull(source.getDataDevolucao())) {
            target.setDataDevolucao(DateUtils.asDate(source.getDataDevolucao()));
        }

        if (Objects.nonNull(source.getDataEnvio())) {
            target.setDataEnvio(DateUtils.asDate(source.getDataEnvio()));
        }

        if (Objects.nonNull(source.getDataInicioProcessamento())) {
            target.setDataInicioProcessamento(DateUtils.asDate(source.getDataInicioProcessamento()));
        }

        if (Objects.nonNull(source.getDataFimProcessamento())) {
            target.setDataFimProcessamento(DateUtils.asDate(source.getDataFimProcessamento()));
        }

        return target;
    }

}
