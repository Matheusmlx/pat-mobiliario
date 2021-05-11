package br.com.azi.patrimoniomobiliario.domain.gateway.integration;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;

import java.util.List;

public interface SistemaDeGestaoAdministrativaIntegration {

    List<UnidadeOrganizacional> buscarUnidadesOrganizacionais();

    List<UnidadeOrganizacional> buscarUnidadesOrganizacionaisPorFuncao(List<String> funcoes);

    List<UnidadeOrganizacional> buscarUnidadeOrganizacionalPorId(Long orgaoId);

    Boolean verificarDominioUnidadeOrganizacionalPorIdEFuncao(Long orgaoId, List<String> funcoes);

    List<UnidadeOrganizacional> buscarSetoresPorOrgaos(List<Long> orgaos);

    List<UnidadeOrganizacional> buscarFundosPorEstruturaAdministradoraEFuncao(List<String> funcoes, Long estruturaAdministradoraId);

    List<UnidadeOrganizacional> buscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncao(Long orgaoId, List<String> funcoes, Boolean almoxarifado);

    ListaPaginada<UnidadeOrganizacional> buscarOrgaosPorFiltro(List<String> funcoes, String filtro, long page, long size);
}
