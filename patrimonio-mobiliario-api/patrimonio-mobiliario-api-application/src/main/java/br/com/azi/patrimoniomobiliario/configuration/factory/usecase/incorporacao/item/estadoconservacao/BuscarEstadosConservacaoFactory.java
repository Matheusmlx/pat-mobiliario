package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.incorporacao.item.estadoconservacao;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.EstadoConservacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.estadoconservacao.listagem.BuscarEstadosConservacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.estadoconservacao.listagem.BuscarEstadosConservacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.estadoconservacao.listagem.converter.EstadoConservacaoOutputDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarEstadosConservacaoFactory {

    @Autowired
    private EstadoConservacaoDataProvider estadoConservacaoDataProvider;

    @Bean("BuscarEstadosConservacaoUseCase")
    @DependsOn({"EstadoConservacaoOutputDataConverter"})
    public BuscarEstadosConservacaoUseCase createUseCase(EstadoConservacaoOutputDataConverter outputDataConverter) {
        return new BuscarEstadosConservacaoUseCaseImpl(estadoConservacaoDataProvider, outputDataConverter);
    }

    @Bean("EstadoConservacaoOutputDataConverter")
    public EstadoConservacaoOutputDataConverter createOutputDataConverter() {
        return new EstadoConservacaoOutputDataConverter();
    }
}
