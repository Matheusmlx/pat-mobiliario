package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.relatorios.termoresponsabilidade;


import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.termoresponsabilidade.GerarTermoGuardaResponsabilidadeInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.termoresponsabilidade.GerarTermoGuardaResponsabilidadeOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.termoresponsabilidade.GerarTermoGuardaResponsabilidadeUseCase;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/movimentacao/{id}/termo-guarda-responsabilidade")
public class GerarTermoGuardaResponsabilidadeMovimentacaoInternaController {

    @Autowired
    private GerarTermoGuardaResponsabilidadeUseCase useCase;

    @GetMapping
    public void executar(@PathVariable Long id, HttpServletResponse httpServletResponse) throws IOException {
        GerarTermoGuardaResponsabilidadeOutputData outputData = useCase.executar(new GerarTermoGuardaResponsabilidadeInputData(id));

        prepareDownloadResponse(outputData,httpServletResponse);
    }

    private void prepareDownloadResponse(GerarTermoGuardaResponsabilidadeOutputData arquivo, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType(arquivo.getContentType());
        httpServletResponse.setHeader("content-disposition", "attachment; filename=\"" + arquivo.getNome() + "\"");

        ByteArrayInputStream byt = new ByteArrayInputStream(arquivo.getContent());

        IOUtils.copy(byt, httpServletResponse.getOutputStream());
        httpServletResponse.flushBuffer();
    }
}
