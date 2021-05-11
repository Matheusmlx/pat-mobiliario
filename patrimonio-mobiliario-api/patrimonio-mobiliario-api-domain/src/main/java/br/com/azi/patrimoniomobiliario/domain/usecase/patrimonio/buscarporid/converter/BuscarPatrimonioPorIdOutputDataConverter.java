package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.buscarporid.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ConfiguracaoDepreciacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.buscarporid.BuscarPatrimonioPorIdOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;

import java.math.BigDecimal;
import java.util.Objects;

public class BuscarPatrimonioPorIdOutputDataConverter extends GenericConverter<Patrimonio, BuscarPatrimonioPorIdOutputData> {

    public BuscarPatrimonioPorIdOutputData to(Patrimonio source, ItemIncorporacao itemIncorporacao, Incorporacao incorporacao, ConfiguracaoDepreciacao configuracaoDepreciacao) {
        BuscarPatrimonioPorIdOutputData target = super.to(source);

        target.setValorLiquido(source.getValorLiquido());

        if (Objects.nonNull(incorporacao.getDataFinalizacao())) {
            target.setDataIncorporado(incorporacao.getDataFinalizacao());
        }

        if (Objects.nonNull(itemIncorporacao.getDescricao())) {
            target.setDescricao(itemIncorporacao.getDescricao());
        }

        if (Objects.nonNull(source.getSituacao())) {
            target.setSituacao(source.getSituacao().name());
        }

        if (Objects.nonNull(source.getOrgao())) {
            target.setOrgao(source.getOrgao().getSigla());
        }

        if (Objects.nonNull(source.getSetor())) {
            target.setSetor(String.format("%s - %s", source.getSetor().getSigla(), source.getSetor().getNome()));
        }

        if (Objects.nonNull(configuracaoDepreciacao)) {
            target.setDadosDepreciacao(BuscarPatrimonioPorIdOutputData.
                DadosDepreciacao
                .builder()
                .id(configuracaoDepreciacao.getId())
                .metodo("QUOTAS_CONSTANTES")
                .vidaUtil(configuracaoDepreciacao.getVidaUtil())
                .valorDepreciacaoAcumulado(getValorDepreciacaoAcumulado(source))
                .valorDepreciacaoMensal(source.getValorDepreciacaoMensal())
                .build());
        }


        if (Objects.nonNull(incorporacao.getReconhecimento())) {
            target.setReconhecimento(incorporacao.getReconhecimento().getNome());
        }

        if (Objects.nonNull(source.getConvenio())) {
            target.setConvenio(source.getConvenio().getNome());
        }

        if (Objects.nonNull(source.getFundo())) {
            target.setFundo(source.getFundo().getSigla());
        }

        if (Objects.nonNull(source.getComodante()) && Objects.nonNull(source.getComodante().getNome())) {
            target.setComodante(source.getComodante().getNome());
        }

        if (Objects.nonNull(source.getNaturezaDespesa())) {
            target.setNaturezaDespesa(source.getNaturezaDespesa().getDespesa() + " - " + source.getNaturezaDespesa().getDescricao());
        }

        if (Objects.nonNull(source.getContaContabilClassificacao())) {
            target.setContaContabilClassificacao(source.getContaContabilClassificacao().getCodigo() + " - " + itemIncorporacao.getContaContabil().getDescricao());
        }

        if (Objects.nonNull(source.getContaContabilAtual())) {
            target.setContaContabilAtual(source.getContaContabilAtual().getCodigo() + " - " + source.getContaContabilAtual().getDescricao());
        }

        if (Objects.nonNull(itemIncorporacao.getTipoBem())) {
            target.setTipo(itemIncorporacao.getTipoBem());
        }

        if (Objects.nonNull(source.getEstadoConservacao())) {
            target.setEstadoConservacao(source.getEstadoConservacao().getNome());
        }

        if (Objects.nonNull(itemIncorporacao.getMarca())) {
            target.setMarca(itemIncorporacao.getMarca());
        }

        if (Objects.nonNull(itemIncorporacao.getModelo())) {
            target.setModelo(itemIncorporacao.getModelo());
        }

        if (Objects.nonNull(itemIncorporacao.getFabricante())) {
            target.setFabricante(itemIncorporacao.getFabricante());
        }

        if (Objects.nonNull(itemIncorporacao.getAnoFabricacaoModelo())) {
            target.setAnoFabricacaoModelo(itemIncorporacao.getAnoFabricacaoModelo());
        }

        if (Objects.nonNull(itemIncorporacao.getCombustivel())) {
            target.setCombustivel(itemIncorporacao.getCombustivel().name());
        }

        if (Objects.nonNull(itemIncorporacao.getCategoria())) {
            target.setCategoria(itemIncorporacao.getCategoria().name());
        }

        if (Objects.nonNull(source.getContaContabilClassificacao().getConfigContaContabil())) {
            target.setPrazoVidaUtil(source.getContaContabilClassificacao().getConfigContaContabil().getVidaUtil());
        }

        if (Objects.nonNull(source.getContaContabilClassificacao().getConfigContaContabil()) &&
            Objects.nonNull(source.getContaContabilClassificacao().getConfigContaContabil().getMetodo())) {
            target.setMetodo(source.getContaContabilClassificacao().getConfigContaContabil().getMetodo().name());
        }

        target.setIncorporacao(BuscarPatrimonioPorIdOutputData.Incorporacao.builder()
            .incorporacaoId(incorporacao.getId())
            .tipo(BuscarPatrimonioPorIdOutputData.Incorporacao.Tipo.INCORPORACAO)
            .valorTotal(source.getValorAquisicao())
            .situacao(incorporacao.getSituacao().toString())
            .numeroNotaLancamentoContabil(incorporacao.getNota())
            .dataFinalizacao(DateValidate.formatarData(incorporacao.getDataFinalizacao()))
            .dataCriacao(DateValidate.formatarData(incorporacao.getDataCadastro()))
            .build());

        return target;
    }

    private BigDecimal getValorDepreciacaoAcumulado(Patrimonio patrimonio) {
        return patrimonio.getValorAquisicao().subtract(patrimonio.getValorLiquido());
    }
}
