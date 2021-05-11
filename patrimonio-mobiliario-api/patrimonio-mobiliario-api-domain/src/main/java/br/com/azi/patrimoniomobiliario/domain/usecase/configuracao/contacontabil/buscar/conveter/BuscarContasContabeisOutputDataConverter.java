package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.buscar.conveter;

import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.buscar.BuscarContasContabeisOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.Objects;
import java.util.stream.Collectors;

public class BuscarContasContabeisOutputDataConverter {
    public BuscarContasContabeisOutputData to(ListaPaginada<ContaContabil> from){
        BuscarContasContabeisOutputDataItemConverter outputDataItemConverter = new BuscarContasContabeisOutputDataItemConverter();

        return BuscarContasContabeisOutputData
            .builder()
            .totalElements(from.getTotalElements())
            .totalPages(from.getTotalPages())
            .items(from
                .getItems()
                .stream()
                .map(outputDataItemConverter::to)
                .collect(Collectors.toList()))
            .build();
    }
    private static class BuscarContasContabeisOutputDataItemConverter extends GenericConverter<ContaContabil, BuscarContasContabeisOutputData.Item> {

        @Override
        public BuscarContasContabeisOutputData.Item to(ContaContabil source) {
            BuscarContasContabeisOutputData.Item target = super.to(source);

            target.setDescricao(source.getDescricao());

            if(Objects.nonNull(source.getId())){
                target.setId(source.getId());
            }

            if(Objects.nonNull(source.getCodigo())){
                target.setId(source.getId());
            }

            if(Objects.nonNull(source.getSituacao())){
                target.setSituacao(source.getSituacao().name());
            }

            if(Objects.nonNull(source.getId())){
                target.setId(source.getId());
            }

            if (Objects.nonNull(source.getConfigContaContabil()) && Objects.nonNull(source.getConfigContaContabil().getTipo())) {
                target.setTipo(source.getConfigContaContabil().getTipo().name());
            }
            if (Objects.nonNull(source.getConfigContaContabil()) && Objects.nonNull(source.getConfigContaContabil().getTipoBem())) {
                target.setTipoBem(source.getConfigContaContabil().getTipoBem().name());
            }

            if (Objects.nonNull(source.getConfigContaContabil()) && Objects.nonNull(source.getConfigContaContabil().getVidaUtil())) {
                target.setVidaUtil(source.getConfigContaContabil().getVidaUtil());
            }

            if (Objects.nonNull(source.getConfigContaContabil()) && Objects.nonNull(source.getConfigContaContabil().getPercentualResidual())) {
                target.setPercentualResidual(source.getConfigContaContabil().getPercentualResidual());
            }

            return target;
        }
    }
}
