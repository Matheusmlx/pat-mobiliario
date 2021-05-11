package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.reserva.pororgao;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximointervalo.BuscarProximoIntervaloDisponivelPorOrgaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximointervalo.BuscarProximoIntervaloDisponivelPorOrgaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.pororgao.buscarproximointervalo.BuscarProximoIntervaloDisponivelPorOrgaoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/configuracao/reservas/proximoIntervalo")
@ConditionalOnProperty(name = "az.patrimonio-mobiliario.reserva-patrimonial-global", havingValue = "false")
public class BuscarProximoIntervaloDisponivelPorOrgaoController {

    private final BuscarProximoIntervaloDisponivelPorOrgaoUseCase useCase;

    @PostMapping
    public BuscarProximoIntervaloDisponivelPorOrgaoOutputData buscarProximoIntervaloDisponivelPorOrgaoOutputData(@RequestBody BuscarProximoIntervaloDisponivelPorOrgaoInputData inputData) {
        return useCase.executar(inputData);
    }
}
