package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.reserva.global.intervalo;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.remover.RemoverReservaIntervaloInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.remover.RemoverReservaIntervaloUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/configuracao/reservas/{id}/intervalos/{intervaloId}")
@ConditionalOnProperty(name = "az.patrimonio-mobiliario.reserva-patrimonial-global", havingValue = "true")
public class RemoverReservaIntervaloController {

    private final RemoverReservaIntervaloUseCase useCase;

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerIntervalo(@PathVariable("intervaloId") Long intervaloId) {
        useCase.executar(new RemoverReservaIntervaloInputData(intervaloId));
    }

}
