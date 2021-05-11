package br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarorgao;

import br.com.azi.patrimoniomobiliario.domain.constant.permissoes.PermissaoMobiliarioConstants;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarorgao.converter.BuscarUnidadesOrganizacionaisOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
public class BuscarUnidadesOrganizacionaisUseCaseImpl implements BuscarUnidadesOrganizacionaisUseCase {

    private final SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    private final BuscarUnidadesOrganizacionaisOutputDataConverter outputDataConverter;

    @Override
    public BuscarUnidadesOrganizacionaisOutputData executar() {
        List<UnidadeOrganizacional> entidadesEncontradas = buscar();

        return outputDataConverter.to(entidadesEncontradas);
    }

    private List<UnidadeOrganizacional> buscar() {
        List<String> funcoes = new ArrayList<>();
        funcoes.add(PermissaoMobiliarioConstants.NORMAL.getDescription());
        funcoes.add(PermissaoMobiliarioConstants.CONSULTA.getDescription());

        return sistemaDeGestaoAdministrativaIntegration.buscarUnidadesOrganizacionaisPorFuncao(funcoes);
    }
}
