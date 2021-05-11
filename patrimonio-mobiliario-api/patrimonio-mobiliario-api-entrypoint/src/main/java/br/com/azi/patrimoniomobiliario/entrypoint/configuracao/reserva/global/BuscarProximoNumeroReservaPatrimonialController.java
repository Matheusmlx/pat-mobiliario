package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.reserva.global;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarproximonumero.BuscarProximoNumeroReservaPatrimonialOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.buscarproximonumero.BuscarProximoNumeroReservaPatrimonialUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/configuracao/reservas/proximoNumero")
@ConditionalOnProperty(name = "az.patrimonio-mobiliario.reserva-patrimonial-global", havingValue = "true")
public class BuscarProximoNumeroReservaPatrimonialController {

    private final BuscarProximoNumeroReservaPatrimonialUseCase useCase;

    @GetMapping
    public BuscarProximoNumeroReservaPatrimonialOutputData buscarProximoNumero() {
        return useCase.executar();
    }

}
