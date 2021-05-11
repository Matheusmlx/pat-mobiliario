package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.reserva.global;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.EditarReservaPatrimonialInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.EditarReservaPatrimonialOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.global.edicao.EditarReservaPatrimonialUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/configuracao/reservas")
@ConditionalOnProperty(name = "az.patrimonio-mobiliario.reserva-patrimonial-global", havingValue = "true")
public class EditarReservaPatrimonialController {

    private final EditarReservaPatrimonialUseCase useCase;

    @PutMapping("/{id}")
    @Transactional
    public EditarReservaPatrimonialOutputData editarReserva(
        @PathVariable(name = "id") Long id, @RequestBody EditarReservaPatrimonialInputData inputData) {

        inputData.setId(id);
        return useCase.executar(inputData);
    }

}
