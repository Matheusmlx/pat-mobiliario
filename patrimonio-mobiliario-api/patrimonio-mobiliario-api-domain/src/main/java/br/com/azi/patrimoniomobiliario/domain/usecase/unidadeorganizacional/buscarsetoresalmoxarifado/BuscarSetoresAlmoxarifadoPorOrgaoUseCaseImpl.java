package br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresalmoxarifado;

import br.com.azi.patrimoniomobiliario.domain.constant.permissoes.PermissaoMobiliarioConstants;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarsetoresalmoxarifado.converter.BuscarSetoresAlmoxarifadoPorOrgaoOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class BuscarSetoresAlmoxarifadoPorOrgaoUseCaseImpl implements BuscarSetoresAlmoxarifadoPorOrgaoUseCase {

    private final SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    BuscarSetoresAlmoxarifadoPorOrgaoOutputDataConverter outputDataConverter;

    @Override
    public BuscarSetoresAlmoxarifadoPorOrgaoOutputData executar(BuscarSetoresAlmoxarifadoPorOrgaoInputData inputData) {

        validarDadosEntrada(inputData);

        List<UnidadeOrganizacional> itens = buscar(inputData);
        return outputDataConverter.to(itens);
    }

    private void validarDadosEntrada(BuscarSetoresAlmoxarifadoPorOrgaoInputData entrada) {
        Validator.of(entrada)
            .validate(BuscarSetoresAlmoxarifadoPorOrgaoInputData::getIdOrgao, Objects::nonNull, "O id é nulo")
            .get();
    }

    private List<UnidadeOrganizacional> buscar(BuscarSetoresAlmoxarifadoPorOrgaoInputData inputData) {
        List<String> funcoes = new ArrayList<>();
        funcoes.add(PermissaoMobiliarioConstants.CONSULTA.getDescription());

        return sistemaDeGestaoAdministrativaIntegration
            .buscarSetoresQueNaoSaoAlmoxarifadoPorOrgaoEFuncao(inputData.getIdOrgao(), funcoes, Boolean.TRUE);
    }
}
