package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.LancamentoContabil;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.LancamentoContabilEntity;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class LancamentoContabilConverter extends GenericConverter<LancamentoContabilEntity, LancamentoContabil> {

    @Autowired
    UnidadeOrganizacionalConverter unidadeOrganizacionalConverter;

    @Autowired
    ContaContabilConverter contaContabilConverter;

    @Autowired
    private PatrimonioConverter patrimonioConverter;

    @Autowired
    private IncorporacaoConverter incorporacaoConverter;

    @Autowired
    private MovimentacaoConverter movimentacaoConverter;

    @Override
    public LancamentoContabil to(LancamentoContabilEntity source) {
        LancamentoContabil target = super.to(source);

        if (Objects.nonNull(source.getOrgao())) {
            target.setOrgao(unidadeOrganizacionalConverter.to(source.getOrgao()));
        }

        if (Objects.nonNull(source.getSetor())) {
            target.setSetor(unidadeOrganizacionalConverter.to(source.getSetor()));
        }

        if (Objects.nonNull(source.getContaContabil())) {
            target.setContaContabil(contaContabilConverter.to(source.getContaContabil()));
        }

        if (Objects.nonNull(source.getPatrimonio())) {
            target.setPatrimonio(patrimonioConverter.to(source.getPatrimonio()));
        }

        if (Objects.nonNull(source.getIncorporacao())) {
            target.setIncorporacao(incorporacaoConverter.to(source.getIncorporacao()));
        }

        if (Objects.nonNull(source.getMovimentacao())) {
            target.setMovimentacao(movimentacaoConverter.to(source.getMovimentacao()));
        }

        if (Objects.nonNull(source.getDataLancamento())) {
            target.setDataLancamento(DateUtils.asLocalDateTime(source.getDataLancamento()));
        }

        return target;
    }

    @Override
    public LancamentoContabilEntity from(LancamentoContabil source) {
        LancamentoContabilEntity target = super.from(source);

        if (Objects.nonNull(source.getOrgao())) {
            target.setOrgao(unidadeOrganizacionalConverter.from(source.getOrgao()));
        }

        if (Objects.nonNull(source.getSetor())) {
            target.setSetor(unidadeOrganizacionalConverter.from(source.getSetor()));
        }

        if (Objects.nonNull(source.getContaContabil())) {
            target.setContaContabil(contaContabilConverter.from(source.getContaContabil()));
        }

        if (Objects.nonNull(source.getPatrimonio())) {
            target.setPatrimonio(patrimonioConverter.from(source.getPatrimonio()));
        }

        if (Objects.nonNull(source.getIncorporacao())) {
            target.setIncorporacao(incorporacaoConverter.from(source.getIncorporacao()));
        }

        if (Objects.nonNull(source.getMovimentacao())) {
            target.setMovimentacao(movimentacaoConverter.from(source.getMovimentacao()));
        }

        if (Objects.nonNull(source.getDataLancamento())) {
            target.setDataLancamento(DateUtils.asDate(source.getDataLancamento()));
        }

        return target;
    }
}
