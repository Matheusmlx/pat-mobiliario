package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.patrimonios;

import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.cadastrar.CadastrarPatrimonioInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.cadastrar.CadastrarPatrimonioUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/incorporacao/items/patrimonios")
public class CadastrarPatrimonioController {

    @Autowired
    CadastrarPatrimonioUseCase useCase;

    @PostMapping
    public ResponseEntity executar(@RequestBody CadastrarPatrimonioInputData inputData) {
        useCase.executar(inputData);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
