package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.reserva;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.listagem.BuscarReservasInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.listagem.BuscarReservasOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.listagem.BuscarReservasUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/configuracao/reservas")
public class BuscarReservasController {

    private final BuscarReservasUseCase useCase;

    @GetMapping
    public BuscarReservasOutputData buscarReservas(BuscarReservasInputData inputData) {
        return useCase.executar(inputData);
    }

}
