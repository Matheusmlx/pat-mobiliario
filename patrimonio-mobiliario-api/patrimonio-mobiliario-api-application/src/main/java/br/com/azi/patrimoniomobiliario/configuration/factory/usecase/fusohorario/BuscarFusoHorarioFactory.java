package br.com.azi.patrimoniomobiliario.configuration.factory.usecase.fusohorario;

import br.com.azi.patrimoniomobiliario.domain.usecase.fusohorario.buscar.BuscarFusoHorarioUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.fusohorario.buscar.BuscarFusoHorarioUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.fusohorario.buscar.converter.BuscarFusoHorarioOutputDataConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class BuscarFusoHorarioFactory {

    @Bean("BuscarFusoHorarioUseCase")
    @DependsOn("BuscarFusoHorarioOutputDataConverter")
    public BuscarFusoHorarioUseCase createUseCase(BuscarFusoHorarioOutputDataConverter outputDataConverter){
        return new BuscarFusoHorarioUseCaseImpl(outputDataConverter);
    }

    @Bean("BuscarFusoHorarioOutputDataConverter")
    public BuscarFusoHorarioOutputDataConverter createConverter(){return new BuscarFusoHorarioOutputDataConverter();}
}
