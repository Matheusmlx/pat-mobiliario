package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.reserva.global;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarporid.BuscarReservaPorIdInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarporid.BuscarReservaPorIdOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarporid.BuscarReservaPorIdUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/configuracao/reservas")
@ConditionalOnProperty(name = "az.patrimonio-mobiliario.reserva-patrimonial-global", havingValue = "true")
public class BuscarReservaPorIdController {

    private final BuscarReservaPorIdUseCase useCase;

    @GetMapping("/{id}")
    public BuscarReservaPorIdOutputData buscarPorId(@PathVariable("id") Long id) {
        return useCase.executar(new BuscarReservaPorIdInputData(id));
    }

}
