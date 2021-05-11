package br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarorgaoporfiltro;

import br.com.azi.patrimoniomobiliario.domain.constant.permissoes.PermissaoMobiliarioConstants;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarorgaoporfiltro.converter.BuscarOrgaoPorFiltroOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class BuscarOrgaoPorFiltroUseCaseImpl implements BuscarOrgaoPorFiltroUseCase {

    private final SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    private final BuscarOrgaoPorFiltroOutputDataConverter outputDataConverter;

    @Override
    public BuscarOrgaoPorFiltroOutputData executar(BuscarOrgaoPorFiltroInputData inputData) {
        ListaPaginada<UnidadeOrganizacional> entidadesEncontradas = buscar(inputData);

        return outputDataConverter.to(entidadesEncontradas);
    }

    private ListaPaginada<UnidadeOrganizacional> buscar(BuscarOrgaoPorFiltroInputData inputData) {
        List<String> funcoes = new ArrayList<>();
        funcoes.add(PermissaoMobiliarioConstants.NORMAL.getDescription());
        funcoes.add(PermissaoMobiliarioConstants.CONSULTA.getDescription());

        return sistemaDeGestaoAdministrativaIntegration.buscarOrgaosPorFiltro(
            funcoes,
            inputData.getConteudo(),
            inputData.getPage() - 1,
            inputData.getSize()
        );
    }

}
