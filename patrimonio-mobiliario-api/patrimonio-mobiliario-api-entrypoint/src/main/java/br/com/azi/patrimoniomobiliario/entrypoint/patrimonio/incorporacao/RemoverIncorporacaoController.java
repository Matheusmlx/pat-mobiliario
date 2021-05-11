package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.remocao.RemoverIncorporacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.remocao.RemoverIncorporacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/incorporacao/{id}")
@Transactional
public class RemoverIncorporacaoController {

    @Autowired
    private RemoverIncorporacaoUseCase useCase;

    @DeleteMapping
    public ResponseEntity executar(RemoverIncorporacaoInputData inputData) {
        useCase.executar(inputData);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
