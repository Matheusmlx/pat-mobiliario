package br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresnaoalmoxarifado;

import br.com.azi.patrimoniomobiliario.domain.constant.permissoes.PermissaoMobiliarioConstants;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresnaoalmoxarifado.converter.BuscarSetoresNaoAlmoxarifadoOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class BuscarSetoresNaoAlmoxarifadoUseCaseImpl implements BuscarSetoresNaoAlmoxarifadoUseCase {

    private final SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    private final BuscarSetoresNaoAlmoxarifadoOutputDataConverter converter;

    @Override
    public BuscarSetoresNaoAlmoxarifadoOutputData executar(BuscarSetoresNaoAlmoxarifadoInputData inputData) {
        validarDadosEntrada(inputData);
        List<UnidadeOrganizacional> unidadesOrganizacionais = buscar(inputData);

        return converter.to(unidadesOrganizacionais);
    }

    private void validarDadosEntrada(BuscarSetoresNaoAlmoxarifadoInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarSetoresNaoAlmoxarifadoInputData::getIdOrgao, Objects::nonNull, "O id do órgão é nulo")
            .get();
    }


    private List<UnidadeOrganizacional> buscar(BuscarSetoresNaoAlmoxarifadoInputData inputData) {
        List<String> funcoes = new ArrayList<>();
        funcoes.add(PermissaoMobiliarioConstants.CONSULTA.getDescription());

        return sistemaDeGestaoAdministrativaIntegration
            .buscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncao(inputData.getIdOrgao(), funcoes, Boolean.FALSE);
    }
}
