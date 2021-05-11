package br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarorgaosporfiltro.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarorgaosporfiltro.entity.BuscarOrgaosPorFuncaoPaginadoIntegrationResponse;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BuscarOrgaosPorFuncaoPaginadoIntegrationConverter {
    private static final BuscarOrgaosPorFuncaoPaginadoIntegrationConverter.ItemOutputDataConverter ITEM_OUTPUT_DATA_CONVERTER = new BuscarOrgaosPorFuncaoPaginadoIntegrationConverter.ItemOutputDataConverter();

    public ListaPaginada<UnidadeOrganizacional> to(BuscarOrgaosPorFuncaoPaginadoIntegrationResponse source) {
        return ListaPaginada.<UnidadeOrganizacional>builder()
            .items(
                source.getContent()
                    .stream()
                    .map(this::converterItem)
                    .collect(Collectors.toList())
            )
            .totalElements((long) source.getTotalElements())
            .totalPages((long) source.getTotalPages())
            .build();
    }

    private UnidadeOrganizacional converterItem(BuscarOrgaosPorFuncaoPaginadoIntegrationResponse.EstruturaOrganizacional source) {
        UnidadeOrganizacional target = ITEM_OUTPUT_DATA_CONVERTER.to(source);

        target.setSigla(source.getSigla());
        target.setCodHierarquia(source.getCodigoHierarquia());
        target.setId(source.getId());
        target.setNome(source.getNome());
        target.setDescricao(String.format("%s - %s", source.getSigla(), source.getNome()));

        return target;
    }

    private static class ItemOutputDataConverter extends GenericConverter<BuscarOrgaosPorFuncaoPaginadoIntegrationResponse.EstruturaOrganizacional, UnidadeOrganizacional> {
    }
}
