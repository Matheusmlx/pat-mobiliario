package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.temporaria;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.visualizacao.BuscarInformacaoDevolucaoPatrimoniosInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.visualizacao.BuscarInformacaoDevolucaoPatrimoniosOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.visualizacao.BuscarInformacaoDevolucaoPatrimoniosUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movimentacoes/internas/temporaria/{id}/visualizacao")
public class BuscarInformacaoDevolucaoPatrimoniosController {

    @Autowired
    private BuscarInformacaoDevolucaoPatrimoniosUseCase useCase;

    @GetMapping
    public BuscarInformacaoDevolucaoPatrimoniosOutputData buscarInformacaoPatrimoniosDevolucao(@PathVariable("id") Long id) {
        return useCase.executar(new BuscarInformacaoDevolucaoPatrimoniosInputData(id));
    }

}
