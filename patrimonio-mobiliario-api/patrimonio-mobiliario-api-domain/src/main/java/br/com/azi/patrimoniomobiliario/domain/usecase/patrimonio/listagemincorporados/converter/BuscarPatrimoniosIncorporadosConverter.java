package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemincorporados.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemincorporados.BuscarPatrimoniosIncorporadosOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

import java.util.Objects;
import java.util.stream.Collectors;

public class BuscarPatrimoniosIncorporadosConverter {

    public BuscarPatrimoniosIncorporadosOutputData to(ListaPaginada<Patrimonio> source) {
        BuscarPatrimonioIncorporadoConverter outputDataConverter = new BuscarPatrimonioIncorporadoConverter();

        return BuscarPatrimoniosIncorporadosOutputData
            .builder()
            .totalElements(source.getTotalElements())
            .totalPages(source.getTotalPages())
            .items(source
                .getItems()
                .stream()
                .map(outputDataConverter::to)
                .collect(Collectors.toList()))
            .build();
    }

    public static class BuscarPatrimonioIncorporadoConverter  extends GenericConverter<Patrimonio, BuscarPatrimoniosIncorporadosOutputData.Item> {

        @Override
        public BuscarPatrimoniosIncorporadosOutputData.Item to(Patrimonio source) {
            BuscarPatrimoniosIncorporadosOutputData.Item target = super.to(source);

            target.setDescricao(source.getNumero() + " - " + source.getItemIncorporacao().getDescricao());

            if (Objects.nonNull(source.getOrgao())) {
                target.setOrgao(source.getOrgao().getSigla());
            }

            if (Objects.nonNull(source.getSetor())) {
                target.setSetor(source.getSetor().getSigla() + " - " + source.getSetor().getNome());
            }

            if (Objects.nonNull(source.getSituacao())) {
                target.setSituacao(source.getSituacao().toString());
            }

            return target;
        }
    }


}
