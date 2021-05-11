package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemincorporados;

import br.com.azi.patrimoniomobiliario.domain.constant.permissoes.PermissaoMobiliarioConstants;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeGestaoAdministrativaIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagem.exception.FiltroIncompletoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemincorporados.converter.BuscarPatrimoniosIncorporadosConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemincorporados.converter.BuscarPatrimoniosIncorporadosFiltroConverter;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BuscarPatrimoniosIncorporadosUseCaseImpl implements BuscarPatrimoniosIncorporadosUseCase {

    private BuscarPatrimoniosIncorporadosFiltroConverter filtroConverter;

    private BuscarPatrimoniosIncorporadosConverter outputDataConverter;

    private SistemaDeGestaoAdministrativaIntegration sistemaDeGestaoAdministrativaIntegration;

    private PatrimonioDataProvider patrimonioDataProvider;

    @Override
    public BuscarPatrimoniosIncorporadosOutputData executar(BuscarPatrimoniosIncorporadosInputData inputData) {
        validarDadosEntrada(inputData);
        Patrimonio.Filtro filtro = criarFiltro(inputData);

        ListaPaginada<Patrimonio> entidadesEncontradas = buscar(filtro);

        return outputDataConverter.to(entidadesEncontradas);
    }

    private void validarDadosEntrada(BuscarPatrimoniosIncorporadosInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarPatrimoniosIncorporadosInputData::getPage, Objects::nonNull, new FiltroIncompletoException("Ausência do número da página"))
            .validate(BuscarPatrimoniosIncorporadosInputData::getSize, size -> Objects.nonNull(size) && size > 0, new FiltroIncompletoException("Ausência da quantidade de itens por página"))
            .get();
    }

    private Patrimonio.Filtro criarFiltro(BuscarPatrimoniosIncorporadosInputData inputData) {
        Patrimonio.Filtro filtro = filtroConverter.to(inputData);
        filtro.setOrgaos(buscarOrgaosIdsAcessoUsuario());
        filtro.setSetores(buscarSetoresIds(filtro.getOrgaos()));
        return filtro;
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

    private List<Long> buscarSetoresIds(List<Long> orgaos) {
        List<UnidadeOrganizacional> setores = sistemaDeGestaoAdministrativaIntegration.buscarSetoresPorOrgaos(orgaos);

        return setores
            .stream()
            .map(UnidadeOrganizacional::getId)
            .collect(Collectors.toList());
    }

    private ListaPaginada<Patrimonio> buscar(Patrimonio.Filtro filtro) {
        return patrimonioDataProvider.buscarPatrimoniosIncorporados(filtro);
    }
}
