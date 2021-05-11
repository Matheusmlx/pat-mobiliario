package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.listagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.listagem.BuscarReservasOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;
import lombok.AllArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.stream.Collectors;

public class BuscarReservasOutputDataConverter {

    public BuscarReservasOutputData to(ListaPaginada<Reserva> source) {
        BuscarReservasItemConverter outputDataConverter = new BuscarReservasItemConverter();

        return BuscarReservasOutputData
            .builder()
            .totalElements(source.getTotalElements())
            .totalPages(source.getTotalPages())
            .items(source
                .getItems()
                .stream()
                .map(outputDataConverter::to)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()))
            .build();
    }

    @AllArgsConstructor
    public static class BuscarReservasItemConverter extends GenericConverter<Reserva, BuscarReservasOutputData.Item> {

        @Override
        public BuscarReservasOutputData.Item to(Reserva source) {
            BuscarReservasOutputData.Item target = super.to(source);

            if (Objects.nonNull(source.getDataCriacao())) {
                target.setDataCriacao(DateValidate.formatarData(source.getDataCriacao()));
            }

            if (!source.getIntervalos().isEmpty()) {
                target.setOrgaos(source.getIntervalos().stream()
                    .map(intervalo -> intervalo.getOrgao().getSigla())
                    .sorted(String::compareTo)
                    .collect(Collectors.toCollection(LinkedHashSet::new))
                );
            }

            return target;
        }
    }

}
