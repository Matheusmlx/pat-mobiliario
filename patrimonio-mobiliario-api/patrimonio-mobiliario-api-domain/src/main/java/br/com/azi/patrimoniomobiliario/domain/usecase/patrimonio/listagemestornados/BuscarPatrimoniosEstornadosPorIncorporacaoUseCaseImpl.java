package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemestornados;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemestornados.converter.BuscarPatrimoniosEstornadosPorIncorporacaoFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemestornados.converter.BuscarPatrimoniosEstornadosPorIncorporacaoOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BuscarPatrimoniosEstornadosPorIncorporacaoUseCaseImpl implements BuscarPatrimoniosEstornadosPorIncorporacaoUseCase {

    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    private PatrimonioDataProvider patrimonioDataProvider;

    private BuscarPatrimoniosEstornadosPorIncorporacaoFiltroConverter filtroConverter;

    private BuscarPatrimoniosEstornadosPorIncorporacaoOutputDataConverter outputDataConverter;

    @Override
    public BuscarPatrimoniosEstornadosPorIncorporacaoOutputData executar(BuscarPatrimoniosEstornadosPorIncorporacaoInputData inputData) {
        validarDadosEntrada(inputData);

        Patrimonio.Filtro filtro = criarFiltro(inputData);

        ListaPaginada<Patrimonio> patrimonios = buscar(filtro);

        return outputDataConverter.to(patrimonios);
    }

    private void validarDadosEntrada(BuscarPatrimoniosEstornadosPorIncorporacaoInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarPatrimoniosEstornadosPorIncorporacaoInputData::getIncorporacaoId, Objects::nonNull, "Id da incorporação é nulo")
            .validate(BuscarPatrimoniosEstornadosPorIncorporacaoInputData::getPage, Objects::nonNull, "Ausência do número da página")
            .validate(BuscarPatrimoniosEstornadosPorIncorporacaoInputData::getSize, size -> Objects.nonNull(size) && size > 0, "Ausência da quantidade de itens por página")
            .get();
    }

    private Patrimonio.Filtro criarFiltro(BuscarPatrimoniosEstornadosPorIncorporacaoInputData inputData) {
        Patrimonio.Filtro filtro = filtroConverter.to(inputData);

        List<Long> itensIncorporacao = buscarItensIncorporacao(inputData.getIncorporacaoId());
        filtro.setItensIncorporacaoId(itensIncorporacao);

        return filtro;
    }

    private List<Long> buscarItensIncorporacao(Long incorporacaoId) {
        return itemIncorporacaoDataProvider.buscarItensPorIncorporacaoId(incorporacaoId)
            .stream()
            .map(ItemIncorporacao::getId)
            .collect(Collectors.toList());
    }

    private ListaPaginada<Patrimonio> buscar(Patrimonio.Filtro filtro) {
        return patrimonioDataProvider.buscarPatrimoniosEstornados(filtro);
    }

}
