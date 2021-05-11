package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.reserva.global.intervalo;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.CadastrarReservaIntervaloInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.intervalo.cadastrar.CadastrarReservaIntervaloUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configuracao/reservas/{id}/intervalos")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "az.patrimonio-mobiliario.reserva-patrimonial-global", havingValue = "true")
public class CadastrarReservaIntervaloController {

    private final CadastrarReservaIntervaloUseCase useCase;

    @PostMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public void inserirReserva(@PathVariable Long id, @RequestBody CadastrarReservaIntervaloInputData inputData) {
        inputData.setReservaId(id);
        useCase.executar(inputData);
    }
}
