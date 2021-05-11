package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.reserva.global;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.remocao.RemoverReservaInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.remocao.RemoverReservaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configuracao/reservas/{id}")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "az.patrimonio-mobiliario.reserva-patrimonial-global", havingValue = "true")
public class RemoverReservaController {

    private final RemoverReservaUseCase useCase;

    @Transactional
    @DeleteMapping
    public void removerReserva(@PathVariable Long id) {
        useCase.executar(new RemoverReservaInputData(id));
    }
}
