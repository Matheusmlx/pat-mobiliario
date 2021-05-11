package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.listagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.listagem.BuscarReservaIntervalosOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.stream.Collectors;

public class BuscarReservaIntervalosOutputDataConverter {

    public BuscarReservaIntervalosOutputData to(ListaPaginada<ReservaIntervalo> source) {
        BuscarOrgaoAdicionadoReservaPatrimonialOutputDataConverter outpuDataConverter = new BuscarOrgaoAdicionadoReservaPatrimonialOutputDataConverter();

        return BuscarReservaIntervalosOutputData
            .builder()
            .items(source
                .getItems()
                .stream()
                .map(outpuDataConverter::to)
                .collect(Collectors.toList()))
            .totalElements(source.getTotalElements())
            .totalPages(source.getTotalPages())
            .build();
    }

    public static class BuscarOrgaoAdicionadoReservaPatrimonialOutputDataConverter extends GenericConverter<ReservaIntervalo, BuscarReservaIntervalosOutputData.Item> {

        @Override
        public BuscarReservaIntervalosOutputData.Item to(ReservaIntervalo source) {
            BuscarReservaIntervalosOutputData.Item target = super.to(source);
            target.setOrgao(String.format("%s - %s", source.getOrgao().getSigla(), source.getOrgao().getNome()));
            target.setPreenchimento(source.getPreenchimento().name());
            return target;
        }

    }

}
