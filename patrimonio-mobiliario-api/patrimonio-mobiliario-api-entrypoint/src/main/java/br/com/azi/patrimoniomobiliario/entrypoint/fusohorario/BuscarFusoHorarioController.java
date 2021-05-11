package br.com.azi.patrimoniomobiliario.entrypoint.fusohorario;

import br.com.azi.patrimoniomobiliario.domain.usecase.fusohorario.buscar.BuscarFusoHorarioOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.fusohorario.buscar.BuscarFusoHorarioUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fuso-horario")
public class BuscarFusoHorarioController {

    @Autowired
    BuscarFusoHorarioUseCase useCase;

    @GetMapping
    BuscarFusoHorarioOutputData executar (){
        return useCase.executar();
    }
}
