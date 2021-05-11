package br.com.azi.patrimoniomobiliario.gateway.integration.hal;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.entity.HalIntegrationProperties;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarfundosporfuncao.BuscarFundosPorFuncaoIntegrationUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarorgaosporfiltro.BuscarOrgaosPorFiltroEFuncaoUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarsetorespororgaos.BuscarSetoresPorOrgaosIntegrationUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarsetoresquenaosaoalmoxarifadopororgaoefuncao.BuscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncaoUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarunidadeorganozacionalporid.BuscarUnidadeOrganizacionalPorIdIntegrationUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarunidadesorganizacionais.BuscarUnidadesOrganizacionaisIntegrationUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.buscarunidadesorganizacionaisporfuncao.BuscarUnidadesOrganizacionaisPorFuncaoIntegrationUseCase;
import br.com.azi.patrimoniomobiliario.gateway.integration.hal.usecase.verificardominiounidadeorganizacionalporidefuncao.VerificarDominioUnidadeOrganizacionalPorIdEFuncaoIntegrationUseCase;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class SistemaDeGestaoAdministrativaIntegrationHalImpl implements SistemaDeGestaoAdministrativaIntegration {

    private HalIntegrationProperties integrationProperties;

    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    private BuscarUnidadesOrganizacionaisIntegrationUseCase buscarUnidadesOrganizacionaisIntegrationUseCase;

    private BuscarUnidadesOrganizacionaisPorFuncaoIntegrationUseCase buscarUnidadesOrganizacionaisPorFuncaoIntegrationUseCase;

    private BuscarUnidadeOrganizacionalPorIdIntegrationUseCase buscarUnidadeOrganizacionalPorIdIntegrationUseCase;

    private VerificarDominioUnidadeOrganizacionalPorIdEFuncaoIntegrationUseCase verificarDominioUnidadeOrganizacionalPorIdEFuncaoIntegrationUseCase;

    private BuscarSetoresPorOrgaosIntegrationUseCase buscarSetoresPorOrgaoIntegrationUseCase;

    private BuscarFundosPorFuncaoIntegrationUseCase buscarFundosPorFuncaoIntegrationUseCase;

    private BuscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncaoUseCase buscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncaoUseCase;

    private BuscarOrgaosPorFiltroEFuncaoUseCase buscarOrgaosPorFiltroEFuncaoUseCase;

    @Override
    public List<UnidadeOrganizacional> buscarUnidadesOrganizacionais() {
        return buscarUnidadesOrganizacionaisIntegrationUseCase.executar(integrationProperties, sessaoUsuarioDataProvider.get());
    }

    @Override
    public List<UnidadeOrganizacional> buscarUnidadesOrganizacionaisPorFuncao(List<String> funcoes) {
        return buscarUnidadesOrganizacionaisPorFuncaoIntegrationUseCase.executar(integrationProperties, sessaoUsuarioDataProvider.get(), funcoes);
    }

    @Override
    public List<UnidadeOrganizacional> buscarUnidadeOrganizacionalPorId(Long orgaoId) {
        return buscarUnidadeOrganizacionalPorIdIntegrationUseCase.executar(integrationProperties, sessaoUsuarioDataProvider.get(), orgaoId);
    }

    @Override
    public Boolean verificarDominioUnidadeOrganizacionalPorIdEFuncao(Long orgaoId, List<String> funcoes) {
        return verificarDominioUnidadeOrganizacionalPorIdEFuncaoIntegrationUseCase.executar(integrationProperties, sessaoUsuarioDataProvider.get(), orgaoId, funcoes);
    }

    @Override
    public List<UnidadeOrganizacional> buscarSetoresPorOrgaos(List<Long> orgaos) {
        return buscarSetoresPorOrgaoIntegrationUseCase.executar(integrationProperties, sessaoUsuarioDataProvider.get(), orgaos);
    }

    @Override
    public List<UnidadeOrganizacional> buscarFundosPorEstruturaAdministradoraEFuncao(List<String> funcoes, Long estruturaAdministradoraId) {
        return buscarFundosPorFuncaoIntegrationUseCase.executar(integrationProperties, sessaoUsuarioDataProvider.get(), funcoes, estruturaAdministradoraId);
    }

    @Override
    public List<UnidadeOrganizacional> buscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncao(Long orgaoId, List<String> funcoes, Boolean almoxarifado) {
        return buscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncaoUseCase.executar(integrationProperties, sessaoUsuarioDataProvider.get(), funcoes, orgaoId, almoxarifado);
    }

    @Override
    public ListaPaginada<UnidadeOrganizacional> buscarOrgaosPorFiltro(List<String> funcoes, String filtro, long page, long size) {
        return buscarOrgaosPorFiltroEFuncaoUseCase.executar(integrationProperties, sessaoUsuarioDataProvider.get(), funcoes, filtro, page, size);
    }

}
