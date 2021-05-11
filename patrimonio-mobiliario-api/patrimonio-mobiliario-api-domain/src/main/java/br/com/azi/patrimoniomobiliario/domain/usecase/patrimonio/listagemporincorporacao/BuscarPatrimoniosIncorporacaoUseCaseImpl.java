package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemporincorporacao;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemporincorporacao.converter.BuscarPatrimoniosIncorporacaoFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagemporincorporacao.converter.BuscarPatrimoniosIncorporacaoOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BuscarPatrimoniosIncorporacaoUseCaseImpl implements BuscarPatrimoniosIncorporacaoUseCase {

    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    private PatrimonioDataProvider patrimonioDataProvider;

    private BuscarPatrimoniosIncorporacaoOutputDataConverter outputDataConverter;

    private BuscarPatrimoniosIncorporacaoFiltroConverter filtroConverter;

    @Override
    public BuscarPatrimoniosIncorporacaoOutputData executar(BuscarPatrimoniosIncorporacaoInputData inputData) {
        validarDadosEntrada(inputData);

        Patrimonio.Filtro filtro = criarFiltro(inputData);
        return outputDataConverter.to(buscarPatrimonios(filtro),buscarQuantidadeTotalPatrimoniosAtivos(inputData));
    }

    private void validarDadosEntrada(BuscarPatrimoniosIncorporacaoInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarPatrimoniosIncorporacaoInputData::getIncorporacaoId, Objects::nonNull, "Id da incorporação é nulo")
            .validate(BuscarPatrimoniosIncorporacaoInputData::getPage, Objects::nonNull, "Número da página é nulo")
            .validate(BuscarPatrimoniosIncorporacaoInputData::getSize, size -> Objects.nonNull(size) && size > 0, "Quantidade de itens por página é nulo")
            .get();
    }

    private Patrimonio.Filtro criarFiltro(BuscarPatrimoniosIncorporacaoInputData inputData) {
        Patrimonio.Filtro filtro = filtroConverter.to(inputData);

        List<Long> itens = buscarItensIncorporacao(inputData);
        filtro.setItensIncorporacaoId(itens);

        return filtro;
    }

    private List<Long> buscarItensIncorporacao(BuscarPatrimoniosIncorporacaoInputData inputData) {
        return itemIncorporacaoDataProvider
            .buscarItensPorIncorporacaoId(inputData.getIncorporacaoId())
            .stream()
            .map(ItemIncorporacao::getId)
            .collect(Collectors.toList());
    }

    private ListaPaginada<Patrimonio> buscarPatrimonios(Patrimonio.Filtro filtro) {
        return patrimonioDataProvider.buscarPatrimoniosPorItensIncorporacao(filtro);
    }

    private Long buscarQuantidadeTotalPatrimoniosAtivos(BuscarPatrimoniosIncorporacaoInputData inputData){
       return patrimonioDataProvider.buscarQuantidadePatrimoniosAtivosPorIncorporacaoId(inputData.getIncorporacaoId());
    }
}


