package br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarfundos;

import br.com.azi.patrimoniomobiliario.domain.constant.permissoes.PermissaoMobiliarioConstants;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.unidadeorganizacional.buscarfundos.converter.BuscarFundosOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@AllArgsConstructor
public class BuscarFundosUseCaseImpl implements BuscarFundosUseCase {

    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    private BuscarFundosOutputDataConverter outputDataConverter;

    @Override
    public BuscarFundosOutputData executar(BuscarFundosInputData inputData) {
        validarDadosEntrada(inputData);
        List<UnidadeOrganizacional> entidadesEncontradas = buscar(inputData);

        return outputDataConverter.to(entidadesEncontradas);
    }

    private void validarDadosEntrada(BuscarFundosInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarFundosInputData::getIdEstruturaAdministradora, Objects::nonNull, "O id da estrutura administradora é nulo!")
            .get();
    }

    private List<UnidadeOrganizacional> buscar(BuscarFundosInputData inputData) {
        List<String> funcoes = new ArrayList<>();
        funcoes.add(PermissaoMobiliarioConstants.CONSULTA.getDescription());

        return sistemaDeGestaoAdministrativaIntegration.buscarFundosPorEstruturaAdministradoraEFuncao(funcoes, inputData.getIdEstruturaAdministradora());
    }
}
