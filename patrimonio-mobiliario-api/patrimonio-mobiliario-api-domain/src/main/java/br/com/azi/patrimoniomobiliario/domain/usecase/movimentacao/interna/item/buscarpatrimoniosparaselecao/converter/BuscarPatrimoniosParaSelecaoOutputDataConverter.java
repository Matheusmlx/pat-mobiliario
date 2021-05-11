package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao.BuscarPatrimoniosParaSelecaoOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.Objects;
import java.util.stream.Collectors;

public class BuscarPatrimoniosParaSelecaoOutputDataConverter {

    public BuscarPatrimoniosParaSelecaoOutputData to(ListaPaginada<Patrimonio> patrimonios) {
        BuscarPatrimonioParaSelecaoOutputDataConverter converter = new BuscarPatrimonioParaSelecaoOutputDataConverter();

        return BuscarPatrimoniosParaSelecaoOutputData
            .builder()
            .items(patrimonios
                .getItems()
                .stream()
                .map(converter::to)
                .collect(Collectors.toList()))
            .totalElements(patrimonios.getTotalElements())
            .totalPages(patrimonios.getTotalPages())
            .build();
    }

    public static class BuscarPatrimonioParaSelecaoOutputDataConverter extends GenericConverter<Patrimonio, BuscarPatrimoniosParaSelecaoOutputData.Item> {

        @Override
        public BuscarPatrimoniosParaSelecaoOutputData.Item to(Patrimonio source) {
            BuscarPatrimoniosParaSelecaoOutputData.Item target = super.to(source);

            target.setPatrimonioId(source.getId());
            target.setPatrimonioNumero(source.getNumero());

            if (Objects.nonNull(source.getItemIncorporacao().getDescricao())) {
                target.setPatrimonioDescricao(source.getItemIncorporacao().getDescricao());
            }

            if (Objects.nonNull(source.getItemIncorporacao().getIncorporacao())) {
                target.setIncorporacaoCodigo(source.getItemIncorporacao().getIncorporacao().getCodigo());
            }

            return target;
        }

    }

}
