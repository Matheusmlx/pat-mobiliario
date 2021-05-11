package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.edicao.EditarIncorporacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.edicao.EditarIncorporacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.edicao.EditarIncorporacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patrimonios/incorporacao/{id}")
public class EditarIncorporacaoController {

    @Autowired
    private EditarIncorporacaoUseCase useCase;

    @PutMapping
    public EditarIncorporacaoOutputData editar(@RequestBody EditarIncorporacaoInputData inputData, @PathVariable Long id) {
        inputData.setId(id);
        return useCase.executar(inputData);
    }
}
