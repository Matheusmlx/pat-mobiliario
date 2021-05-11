package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemestornados.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemestornados.BuscarPatrimoniosEstornadosPorIncorporacaoOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;

import java.util.Objects;
import java.util.stream.Collectors;

public class BuscarPatrimoniosEstornadosPorIncorporacaoOutputDataConverter {

    public BuscarPatrimoniosEstornadosPorIncorporacaoOutputData to(ListaPaginada<Patrimonio> source) {
        BuscarPatrimonioEstornadoConverter converter = new BuscarPatrimonioEstornadoConverter();

        return BuscarPatrimoniosEstornadosPorIncorporacaoOutputData
            .builder()
            .items(
                source
                    .getItems()
                    .stream()
                    .map(converter::to)
                    .collect(Collectors.toList()))
            .totalElements(source.getTotalElements())
            .totalPages(source.getTotalPages())
            .build();
    }

    public static class BuscarPatrimonioEstornadoConverter extends GenericConverter<Patrimonio, BuscarPatrimoniosEstornadosPorIncorporacaoOutputData.Item> {

        @Override
        public BuscarPatrimoniosEstornadosPorIncorporacaoOutputData.Item to(Patrimonio source) {
            BuscarPatrimoniosEstornadosPorIncorporacaoOutputData.Item target = super.to(source);

            if (Objects.nonNull(source.getItemIncorporacao())) {
                target.setDescricao(source.getItemIncorporacao().getDescricao());
            }

            if (Objects.nonNull(source.getValorAquisicao())) {
                target.setValor(source.getValorAquisicao());
            }

            if (Objects.nonNull(source.getUsuarioEstorno())) {
                target.setUsuario(source.getUsuarioEstorno());
            }

            if (Objects.nonNull(source.getMotivoEstorno())) {
                target.setMotivo(source.getMotivoEstorno());
            }

            if (Objects.nonNull(source.getDataEstorno())) {
                target.setData(DateValidate.formatarData(source.getDataEstorno()));
            }

            return target;
        }

    }
}
