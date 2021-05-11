package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Comodante;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ComodanteEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ContaContabilEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ConvenioEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.EstadoConservacaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ItemIncorporacaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.NaturezaDespesaEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.PatrimonioEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.UnidadeOrganizacionalEntity;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PatrimonioConverter extends GenericConverter<PatrimonioEntity, Patrimonio> {

    @Autowired
    ContaContabilConverter contaContabilConverter;

    @Autowired
    NaturezaDespesaConverter naturezaDespesaConverter;

    @Autowired
    EstadoConservacaoConverter estadoConservacaoConverter;

    @Autowired
    ConvenioConverter convenioConverter;

    @Autowired
    IncorporacaoConverter incorporacaoConverter;

    @Autowired
    ItemIncorporacaoConverter itemIncorporacaoConverter;

    @Autowired
    UnidadeOrganizacionalConverter unidadeOrganizacionalConverter;

    @Override
    public PatrimonioEntity from(Patrimonio source) {
        PatrimonioEntity target = super.from(source);

        if (Objects.nonNull(source.getItemIncorporacao())) {
            target.setItemIncorporacao(ItemIncorporacaoEntity
                .builder()
                .id(source.getItemIncorporacao().getId())
                .codigo(source.getItemIncorporacao().getCodigo())
                .build());
        }

        if (Objects.nonNull(source.getContaContabilClassificacao()) && Objects.nonNull(source.getContaContabilClassificacao().getId())) {
            target.setContaContabilClassificacao(
                ContaContabilEntity
                    .builder()
                    .id(source.getContaContabilClassificacao().getId())
                    .build()
            );
        }

        if (Objects.nonNull(source.getContaContabilAtual()) && Objects.nonNull(source.getContaContabilAtual().getId())) {
            target.setContaContabilAtual(
                ContaContabilEntity
                    .builder()
                    .id(source.getContaContabilAtual().getId())
                    .build()
            );
        }

        if (Objects.nonNull(source.getOrgao())) {
            target.setOrgao(UnidadeOrganizacionalEntity.builder().id(source.getOrgao().getId()).build());
        }

        if (Objects.nonNull(source.getSetor())) {
            target.setSetor(UnidadeOrganizacionalEntity.builder().id(source.getSetor().getId()).build());
        }

        if (Objects.nonNull(source.getFundo())) {
            target.setFundo(UnidadeOrganizacionalEntity.builder().id(source.getFundo().getId()).build());
        }

        if (Objects.nonNull(source.getNaturezaDespesa())) {
            target.setNaturezaDespesa(NaturezaDespesaEntity.builder().id(source.getNaturezaDespesa().getId()).build());
        }

        if (Objects.nonNull(source.getEstadoConservacao())) {
            target.setEstadoConservacao(EstadoConservacaoEntity.builder().id(source.getEstadoConservacao().getId()).build());
        }

        if (Objects.nonNull(source.getConvenio())) {
            target.setConvenio(ConvenioEntity.builder().id(source.getConvenio().getId()).build());
        }

        if (Objects.nonNull(source.getDataEstorno())) {
            target.setDataEstorno(DateUtils.asDate(source.getDataEstorno()));
        }

        if (Objects.nonNull(source.getComodante()) && Objects.nonNull(source.getComodante().getId())) {
            target.setComodante(ComodanteEntity
                .builder()
                .id(source.getComodante().getId())
                .nome(source.getComodante().getNome())
                .build());
        }

        if (Objects.nonNull(source.getInicioVidaUtil())) {
            target.setInicioVidaUtil(DateUtils.asDate(source.getInicioVidaUtil()));
        }

        if (Objects.nonNull(source.getFimVidaUtil())) {
            target.setFimVidaUtil(DateUtils.asDate(source.getFimVidaUtil()));
        }

        return target;
    }

    @Override
    public Patrimonio to(PatrimonioEntity source) {
        Patrimonio target = super.to(source);

        if (Objects.nonNull(source.getItemIncorporacao())) {
            target.setItemIncorporacao(itemIncorporacaoConverter.to(source.getItemIncorporacao()));
        }

        if (Objects.nonNull(source.getContaContabilClassificacao())) {
            target.setContaContabilClassificacao(contaContabilConverter.to(source.getContaContabilClassificacao()));
        }

        if (Objects.nonNull(source.getContaContabilAtual())) {
            target.setContaContabilAtual(contaContabilConverter.to(source.getContaContabilAtual()));
        }

        if (Objects.nonNull(source.getOrgao())) {
            target.setOrgao(unidadeOrganizacionalConverter.to(source.getOrgao()));
        }

        if (Objects.nonNull(source.getSetor())) {
            target.setSetor(unidadeOrganizacionalConverter.to(source.getSetor()));
        }

        if (Objects.nonNull(source.getFundo())) {
            target.setFundo(unidadeOrganizacionalConverter.to(source.getFundo()));
        }

        if (Objects.nonNull(source.getNaturezaDespesa())) {
            target.setNaturezaDespesa(naturezaDespesaConverter.to(source.getNaturezaDespesa()));
        }

        if (Objects.nonNull(source.getEstadoConservacao())) {
            target.setEstadoConservacao(estadoConservacaoConverter.to(source.getEstadoConservacao()));
        }

        if (Objects.nonNull(source.getConvenio())) {
            target.setConvenio(convenioConverter.to(source.getConvenio()));
        }

        if (Objects.nonNull(source.getComodante())) {
            target.setComodante(
                Comodante
                    .builder()
                    .id(source.getComodante().getId())
                    .nome(source.getComodante().getNome())
                    .build()
            );
        }

        if (Objects.nonNull(source.getDataEstorno())) {
            target.setDataEstorno(DateUtils.asLocalDateTime(source.getDataEstorno()));
        }

        if (Objects.nonNull(source.getComodante()) && Objects.nonNull(source.getComodante().getId())) {
            target.setComodante(Comodante
                .builder()
                .id(source.getComodante().getId())
                .nome(source.getComodante().getNome())
                .build());
        }

        if (Objects.nonNull(source.getInicioVidaUtil())) {
            target.setInicioVidaUtil(DateUtils.asLocalDateTime(source.getInicioVidaUtil()));
        }

        if (Objects.nonNull(source.getFimVidaUtil())) {
            target.setFimVidaUtil(DateUtils.asLocalDateTime(source.getFimVidaUtil()));
        }

        return target;
    }
}
