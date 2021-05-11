package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;

import java.util.List;

public interface UnidadeOrganizacionalDataProvider {
    UnidadeOrganizacional buscarUnidadeOrganizacionalPorNome(String nome);
    UnidadeOrganizacional buscarUnidadeOrganizacionalPorId(Long id);
    List<UnidadeOrganizacional> buscarSetoresAlmoxarifadoPorOrgao(Long id);
}
