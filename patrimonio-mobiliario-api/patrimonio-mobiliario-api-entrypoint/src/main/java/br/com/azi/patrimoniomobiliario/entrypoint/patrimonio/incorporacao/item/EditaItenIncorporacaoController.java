package br.com.azi.patrimoniomobiliario.entrypoint.patrimonio.incorporacao.item;

import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.EditaItemIncorporacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.EditaItemIncorporacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.editar.EditaItemIncorporacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Transactional
@RequestMapping("/patrimonios/incorporacao/{idIncorporacao}/item/{id}")
public class EditaItenIncorporacaoController {

    @Autowired
    private EditaItemIncorporacaoUseCase useCase;

    @PutMapping
    public EditaItemIncorporacaoOutputData executar(@PathVariable Long id, @PathVariable Long idIncorporacao, @RequestBody EditaItemIncorporacaoInputData inputData){
        inputData.setId(id);
        inputData.setIdIncorporacao(idIncorporacao);
        return useCase.executar(inputData);
    }
}
