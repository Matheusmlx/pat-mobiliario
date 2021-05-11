package br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetores;

import br.com.azi.patrimoniomobiliario.domain.constant.permissoes.PermissaoMobiliarioConstants;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetores.converter.BuscarSetoresOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
public class BuscarSetoresUseCaseImpl implements BuscarSetoresUseCase {

    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    private BuscarSetoresOutputDataConverter outputDataConverter;

    @Override
    public BuscarSetoresOutputData executar() {
        List<Long> orgaosIdAcessoUsuario = buscarOrgaosIdsAcessoUsuario();
        List<UnidadeOrganizacional> setoresAcessoUsuario = buscarSetoresId(orgaosIdAcessoUsuario);

        return outputDataConverter.to(setoresAcessoUsuario);
    }

    private List<Long> buscarOrgaosIdsAcessoUsuario() {
        List<String> funcoes = new ArrayList<>();
        funcoes.add(PermissaoMobiliarioConstants.NORMAL.getDescription());
        funcoes.add(PermissaoMobiliarioConstants.CONSULTA.getDescription());

        List<UnidadeOrganizacional> unidadeOrganizacionals = sistemaDeGestaoAdministrativaIntegration.buscarUnidadesOrganizacionaisPorFuncao(funcoes);

        return unidadeOrganizacionals
            .stream()
            .map(UnidadeOrganizacional::getId)
            .collect(Collectors.toList());
    }

    private List<UnidadeOrganizacional> buscarSetoresId(List<Long> orgaos) {
        return sistemaDeGestaoAdministrativaIntegration.buscarSetoresPorOrgaos(orgaos);
    }
}
