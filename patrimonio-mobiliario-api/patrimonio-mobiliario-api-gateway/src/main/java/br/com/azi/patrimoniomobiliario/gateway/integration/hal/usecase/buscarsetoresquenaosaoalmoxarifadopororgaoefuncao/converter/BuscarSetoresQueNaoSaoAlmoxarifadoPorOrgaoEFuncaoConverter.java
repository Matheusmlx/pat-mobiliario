package br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarsetoresquenaosaoalmoxarifadopororgaoefuncao.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarsetoresquenaosaoalmoxarifadopororgaoefuncao.entity.BuscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncaoIntegrationResponse;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BuscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncaoConverter {

    private static final BuscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncaoConverter.ItemOutputDataConverter ITEM_OUTPUT_DATA_CONVERTER =
        new BuscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncaoConverter.ItemOutputDataConverter();

    public List<UnidadeOrganizacional> to(BuscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncaoIntegrationResponse source) {
        return source.getContent()
            .stream()
            .map(this::converterItem)
            .collect(Collectors.toList());
    }

    private UnidadeOrganizacional converterItem(BuscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncaoIntegrationResponse.EstruturaOrganizacional source) {
        UnidadeOrganizacional target = ITEM_OUTPUT_DATA_CONVERTER.to(source);

        target.setSigla(source.getSigla());
        target.setCodHierarquia(source.getCodigoHierarquia());
        target.setId(source.getId());
        target.setNome(source.getNome());
        target.setDescricao(String.format("%s - %s", source.getSigla(), source.getNome()));

        return target;
    }

    private static class ItemOutputDataConverter extends GenericConverter<BuscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncaoIntegrationResponse.EstruturaOrganizacional, UnidadeOrganizacional> {
    }

}
