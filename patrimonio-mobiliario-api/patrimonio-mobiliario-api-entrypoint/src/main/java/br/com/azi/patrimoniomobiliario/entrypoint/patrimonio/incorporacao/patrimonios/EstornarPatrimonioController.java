package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.patrimonios;

import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.estornar.EstornarPatrimonioInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.estornar.EstornarPatrimonioUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/incorporacao/items/patrimonios/estornar")
public class EstornarPatrimonioController {

    @Autowired
    private EstornarPatrimonioUseCase useCase;

    @PatchMapping
    public ResponseEntity estornar(@RequestBody EstornarPatrimonioInputData inputData){
        useCase.executar(inputData);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
