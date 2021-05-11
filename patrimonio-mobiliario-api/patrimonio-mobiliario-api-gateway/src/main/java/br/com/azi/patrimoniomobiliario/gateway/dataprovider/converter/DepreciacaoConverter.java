package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Depreciacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ContaContabilEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.DepreciacaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.PatrimonioEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.UnidadeOrganizacionalEntity;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DepreciacaoConverter extends GenericConverter<DepreciacaoEntity, Depreciacao> {

    @Override
    public DepreciacaoEntity from(Depreciacao source) {
        DepreciacaoEntity target = super.from(source);

        if (Objects.nonNull(source.getDataInicial())) {
            target.setDataInicial(DateUtils.asDate(source.getDataInicial()));
        }

        if (Objects.nonNull(source.getDataFinal())) {
            target.setDataFinal(DateUtils.asDate(source.getDataFinal()));
        }

        if (Objects.nonNull(source.getPatrimonio())) {
            target.setPatrimonio(PatrimonioEntity.builder()
                .id(source.getPatrimonio().getId())
                .build());
        }

        if (Objects.nonNull(source.getOrgao())) {
            target.setOrgao(UnidadeOrganizacionalEntity
                .builder()
                .id(source.getOrgao().getId())
                .build());
        }

        if (Objects.nonNull(source.getSetor())) {
            target.setSetor(UnidadeOrganizacionalEntity
                .builder()
                .id(source.getSetor().getId())
                .build());
        }

        if (Objects.nonNull(source.getContaContabil())) {
            target.setContaContabil(ContaContabilEntity
                .builder()
                .id(source.getContaContabil().getId())
                .build());
        }

        return target;
    }

    @Override
    public Depreciacao to(DepreciacaoEntity source) {
        Depreciacao target = super.to(source);

        if (Objects.nonNull(source.getDataInicial())) {
            target.setDataInicial(DateUtils.asLocalDateTime(source.getDataInicial()));
        }

        if (Objects.nonNull(source.getDataFinal())) {
            target.setDataFinal(DateUtils.asLocalDateTime(source.getDataFinal()));
        }

        if (Objects.nonNull(source.getPatrimonio())) {
            target.setPatrimonio(Patrimonio
                .builder()
                .id(source.getPatrimonio().getId())
                .build());
        }

        if (Objects.nonNull(source.getOrgao())) {
            target.setOrgao(UnidadeOrganizacional
                .builder()
                .id(source.getOrgao().getId())
                .sigla(source.getOrgao().getSigla())
                .build());
        }

        if (Objects.nonNull(source.getSetor())) {
            target.setSetor(UnidadeOrganizacional
                .builder()
                .id(source.getSetor().getId())
                .sigla(source.getSetor().getSigla())
                .build());
        }

        if (Objects.nonNull(source.getContaContabil())) {
            target.setContaContabil(ContaContabil
                .builder()
                .id(source.getContaContabil().getId())
                .build());
        }

        return target;
    }
}
