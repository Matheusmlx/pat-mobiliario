package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.reserva.global.intervalo;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.validarintervalo.ValidarIntervaloInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.validarintervalo.ValidarIntervaloUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/configuracao/reservas/{id}/intervalos/validar")
@ConditionalOnProperty(name = "az.patrimonio-mobiliario.reserva-patrimonial-global", havingValue = "true")
public class ValidarIntervaloController {

    private final ValidarIntervaloUseCase useCase;

    @PostMapping
    public void buscarProximoNumero(@PathVariable(name = "id") Long reservaId, @RequestBody ValidarIntervaloInputData inputData) {
        inputData.setReservaId(reservaId);
        useCase.executar(inputData);
    }

}
