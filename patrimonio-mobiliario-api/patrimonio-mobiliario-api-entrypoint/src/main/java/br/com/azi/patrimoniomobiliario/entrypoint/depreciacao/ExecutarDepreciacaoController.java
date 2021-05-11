package br.com.azi.patrimoniomobiliario.entrypoint.depreciacao;

import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.DepreciacaoPatrimonial;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/depreciacao")
@RequiredArgsConstructor
public class ExecutarDepreciacaoController {

    private final DepreciacaoPatrimonial depreciacaoPatrimonial;

    @GetMapping
    public ResponseEntity<String> executar() {
        CompletableFuture.runAsync(depreciacaoPatrimonial::executar);
        return new ResponseEntity<>("Depreciação iniciada com sucesso.", HttpStatus.OK);
    }
}
