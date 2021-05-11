package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Comodante;
import br.com.azi.patrimoniomobiliario.domain.entity.Convenio;
import br.com.azi.patrimoniomobiliario.domain.entity.Fornecedor;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Reconhecimento;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ComodanteEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.IncorporacaoEntity;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class IncorporacaoConverter extends GenericConverter<IncorporacaoEntity, Incorporacao> {

    @Autowired
    private ConvenioConverter convenioConverter;

    @Autowired
    private UnidadeOrganizacionalConverter unidadeOrganizacionalConverter;

    @Autowired
    private ReconhecimentoConverter reconhecimentoConverter;

    @Autowired
    private FornecedorConverter fornecedorConverter;

    @Autowired
    private NotaLancamentoContabilConverter notaLancamentoContabilConverter;

    @Override
    public IncorporacaoEntity from(Incorporacao source) {
        IncorporacaoEntity target = super.from(source);
        if (Objects.nonNull(source.getOrgao())) {
            target.setOrgao(unidadeOrganizacionalConverter.from(source.getOrgao()));
        }
        if (Objects.nonNull(source.getSetor())) {
            target.setSetor(unidadeOrganizacionalConverter.from(source.getSetor()));
        }
        if (Objects.nonNull(source.getFundo())) {
            target.setFundo(unidadeOrganizacionalConverter.from(source.getFundo()));
        }
        if (Objects.nonNull(source.getConvenio())) {
            target.setConvenio(convenioConverter.from(source.getConvenio()));
        }
        if (Objects.nonNull(source.getReconhecimento())) {
            target.setReconhecimento(reconhecimentoConverter.from(source.getReconhecimento()));
        }
        if (Objects.nonNull(source.getDataRecebimento())) {
            target.setDataRecebimento(DateUtils.asDate(source.getDataRecebimento()));
        }
        if (Objects.nonNull(source.getDataNota())) {
            target.setDataNota(DateUtils.asDate(source.getDataNota()));
        }
        if (Objects.nonNull(source.getDataFinalizacao())) {
            target.setDataFinalizacao(DateUtils.asDate(source.getDataFinalizacao()));
        }
        if (Objects.nonNull(source.getFornecedor())) {
            target.setFornecedor(fornecedorConverter.from(source.getFornecedor()));
        }
        if (Objects.nonNull(source.getNotaLancamentoContabil())) {
            target.setNotaLancamentoContabil(notaLancamentoContabilConverter.from(source.getNotaLancamentoContabil()));
        }
        if (Objects.nonNull(source.getComodante()) && Objects.nonNull(source.getComodante().getId())) {
            target.setComodanteEntity(ComodanteEntity
                .builder()
                .id(source.getComodante().getId())
                .nome(source.getComodante().getNome())
                .build());
        }
        if (Objects.nonNull(source.getDataInicioProcessamento())) {
            target.setDataInicioProcessamento(DateUtils.asDate(source.getDataInicioProcessamento()));
        }
        if (Objects.nonNull(source.getDataFimProcessamento())) {
            target.setDataFimProcessamento(DateUtils.asDate(source.getDataFimProcessamento()));
        }
        return target;
    }

    @Override
    public Incorporacao to(IncorporacaoEntity source) {
        Incorporacao target = super.to(source);
        if (Objects.nonNull(source.getOrgao())) {
            try {
                target.setOrgao(unidadeOrganizacionalConverter.to(source.getOrgao()));
            } catch (Exception e) {
                target.setOrgao(UnidadeOrganizacional.builder().id(source.getOrgao().getId()).build());
            }
        }
        if (Objects.nonNull(source.getSetor())) {
            try {
                target.setSetor(unidadeOrganizacionalConverter.to(source.getSetor()));
            } catch (Exception e) {
                target.setSetor(UnidadeOrganizacional.builder().id(source.getSetor().getId()).build());
            }
        }
        if (Objects.nonNull(source.getFundo())) {
            try {
                target.setFundo(unidadeOrganizacionalConverter.to(source.getFundo()));
            } catch (Exception e) {
                target.setFundo(UnidadeOrganizacional.builder().id(source.getFundo().getId()).build());
            }
        }
        if (Objects.nonNull(source.getConvenio())) {
            try {
                target.setConvenio(convenioConverter.to(source.getConvenio()));
            } catch (Exception e) {
                target.setConvenio(Convenio.builder().id(source.getConvenio().getId()).build());
            }
        }
        if (Objects.nonNull(source.getReconhecimento())) {
            try {
                target.setReconhecimento(reconhecimentoConverter.to(source.getReconhecimento()));
            } catch (Exception e) {
                target.setReconhecimento(Reconhecimento.builder().id(source.getReconhecimento().getId()).build());
            }
        }
        if (Objects.nonNull(source.getDataRecebimento())) {
            target.setDataRecebimento(DateUtils.asLocalDateTime(source.getDataRecebimento()));
        }
        if (Objects.nonNull(source.getDataNota())) {
            target.setDataNota(DateUtils.asLocalDateTime(source.getDataNota()));
        }
        if (Objects.nonNull(source.getDataFinalizacao())) {
            target.setDataFinalizacao(DateUtils.asLocalDateTime(source.getDataFinalizacao()));
        }
        if (Objects.nonNull(source.getFornecedor())) {
            try {
                target.setFornecedor(fornecedorConverter.to(source.getFornecedor()));
            } catch (Exception e) {
                target.setFornecedor(Fornecedor.builder().id(source.getFornecedor().getId()).build());
            }
        }
        if (Objects.nonNull(source.getNotaLancamentoContabil())) {
            try {
                target.setNotaLancamentoContabil(notaLancamentoContabilConverter.to(source.getNotaLancamentoContabil()));
            } catch (Exception e) {
                target.setNotaLancamentoContabil(NotaLancamentoContabil.builder().
                    id(source.getNotaLancamentoContabil().getId())
                    .build()
                );
            }
        }
        if (Objects.nonNull(source.getComodanteEntity()) && Objects.nonNull(source.getComodanteEntity().getId())) {
            try {
                target.setComodante(Comodante.builder()
                    .id(source.getComodanteEntity().getId())
                    .nome(source.getComodanteEntity().getNome())
                    .build()
                );
            } catch (Exception e) {
                target.setComodante(Comodante.builder()
                    .id(source.getComodanteEntity().getId())
                    .build()
                );
            }
        }
        if (Objects.nonNull(source.getDataInicioProcessamento())) {
            target.setDataInicioProcessamento(DateUtils.asLocalDateTime(source.getDataInicioProcessamento()));
        }
        if (Objects.nonNull(source.getDataFimProcessamento())) {
            target.setDataFimProcessamento(DateUtils.asLocalDateTime(source.getDataFimProcessamento()));
        }
        return target;
    }
}
