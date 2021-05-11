package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.reserva.global.intervalo;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarintervalodisponivel.BuscarIntervaloDisponivelInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarintervalodisponivel.BuscarIntervaloDisponivelOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.buscarintervalodisponivel.BuscarIntervaloDisponivelUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/configuracao/reservas/{id}/intervalos/proximoDisponivel")
@ConditionalOnProperty(name = "az.patrimonio-mobiliario.reserva-patrimonial-global", havingValue = "true")
public class BuscarIntervaloDisponivelController {

    private final BuscarIntervaloDisponivelUseCase useCase;

    @PostMapping
    public BuscarIntervaloDisponivelOutputData buscarIntervaloReserva(
        @PathVariable("id") Long reservaId, @RequestBody BuscarIntervaloDisponivelInputData inputData) {

        inputData.setReservaId(reservaId);
        return useCase.executar(inputData);
    }

}
