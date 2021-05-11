package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.item;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao.BuscarPatrimoniosParaSelecaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao.BuscarPatrimoniosParaSelecaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao.converter.BuscarPatrimoniosParaSelecaoFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.item.buscarpatrimoniosparaselecao.converter.BuscarPatrimoniosParaSelecaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarPatrimoniosParaSelecaoFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Bean("BuscarPatrimoniosParaSelecaoUseCase")
    @DependsOn({
        "BuscarPatrimoniosParaSelecaoFiltroConverter",
        "BuscarPatrimoniosParaSelecaoOutputDataConverter"
    })
    public BuscarPatrimoniosParaSelecaoUseCase createUseCase(BuscarPatrimoniosParaSelecaoFiltroConverter filtroConverter, BuscarPatrimoniosParaSelecaoOutputDataConverter outputDataConverter) {
        return new BuscarPatrimoniosParaSelecaoUseCaseImpl(
            movimentacaoDataProvider,
            patrimonioDataProvider,
            filtroConverter,
            outputDataConverter
        );
    }

    @Bean("BuscarPatrimoniosParaSelecaoFiltroConverter")
    public BuscarPatrimoniosParaSelecaoFiltroConverter createFiltroConverter() {
        return new BuscarPatrimoniosParaSelecaoFiltroConverter();
    }

    @Bean("BuscarPatrimoniosParaSelecaoOutputDataConverter")
    public BuscarPatrimoniosParaSelecaoOutputDataConverter createOutputDataConverter() {
        return new BuscarPatrimoniosParaSelecaoOutputDataConverter();
    }

}
