package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.item;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.exclusao.ExcluirItemIncorporacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.exclusao.ExcluirItemIncorporacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/incorporacao/{idIncorporacao}/item/{id}")
public class ExcluirItemIncorporacaoController {

    @Autowired
    private ExcluirItemIncorporacaoUseCase useCase;

    @DeleteMapping
    public ResponseEntity executar(@PathVariable Long id, @PathVariable Long idIncorporacao, ExcluirItemIncorporacaoInputData inputData) {
        inputData.setId(id);
        inputData.setIdIncorporacao(idIncorporacao);
        useCase.executar(inputData);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}

