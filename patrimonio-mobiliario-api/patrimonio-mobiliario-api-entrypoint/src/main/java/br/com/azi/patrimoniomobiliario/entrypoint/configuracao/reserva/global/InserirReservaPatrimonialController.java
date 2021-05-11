package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.reserva.global;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir.InserirReservaPatrimonialInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir.InserirReservaPatrimonialOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.inserir.InserirReservaPatrimonialUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configuracao/reservas")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "az.patrimonio-mobiliario.reserva-patrimonial-global", havingValue = "true")
public class InserirReservaPatrimonialController {

    private final InserirReservaPatrimonialUseCase useCase;

    @PostMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public InserirReservaPatrimonialOutputData inserirReserva(@RequestBody InserirReservaPatrimonialInputData inputData) {
        return useCase.executar(inputData);
    }

}
