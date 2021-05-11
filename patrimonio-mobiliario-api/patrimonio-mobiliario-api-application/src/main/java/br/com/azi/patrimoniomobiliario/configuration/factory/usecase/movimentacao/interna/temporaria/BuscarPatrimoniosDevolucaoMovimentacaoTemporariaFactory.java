package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.movimentacao.interna.temporaria;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao.BuscarPatrimoniosDevolucaoMovimentacaoTemporariaUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao.BuscarPatrimoniosDevolucaoMovimentacaoTemporariaUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao.converter.BuscarPatrimoniosDevolucaoMovimentacaoTemporariaFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.bucarpatrimoniosdevolucao.converter.BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarPatrimoniosDevolucaoMovimentacaoTemporariaFactory {

    @Autowired
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Autowired
    private PatrimonioDataProvider patrimonioDataProvider;

    @Bean("BuscarPatrimoniosDevolucaoMovimentacaoTemporariaUseCase")
    @DependsOn({"BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputDataConverter", "BuscarPatrimoniosDevolucaoMovimentacaoTemporariaFiltroConverter"})
    public BuscarPatrimoniosDevolucaoMovimentacaoTemporariaUseCase createUseCase(
        BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputDataConverter outputDataConverter,
        BuscarPatrimoniosDevolucaoMovimentacaoTemporariaFiltroConverter filtroConverter) {
        return new BuscarPatrimoniosDevolucaoMovimentacaoTemporariaUseCaseImpl(
            movimentacaoDataProvider,
            patrimonioDataProvider,
            filtroConverter,
            outputDataConverter
        );
    }

    @Bean("BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputDataConverter")
    public BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputDataConverter createOutputDataConverter() {
        return new BuscarPatrimoniosDevolucaoMovimentacaoTemporariaOutputDataConverter();
    }

    @Bean(value = "BuscarPatrimoniosDevolucaoMovimentacaoTemporariaFiltroConverter")
    public BuscarPatrimoniosDevolucaoMovimentacaoTemporariaFiltroConverter createFiltroConverter() {
        return new BuscarPatrimoniosDevolucaoMovimentacaoTemporariaFiltroConverter();
    }

}
