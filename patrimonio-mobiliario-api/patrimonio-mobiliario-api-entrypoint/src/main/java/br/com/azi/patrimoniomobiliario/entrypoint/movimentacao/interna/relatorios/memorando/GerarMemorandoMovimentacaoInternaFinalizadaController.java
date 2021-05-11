package br.com.azi.patrimoniomobiliario.entrypoint.movimentacao.interna.relatorios.memorando;

import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaofinalizada.GerarMemorandoMovimentacaoInternaFinalizadaInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaofinalizada.GerarMemorandoMovimentacaoInternaFinalizadaOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.relatorios.memorando.memorandomovimentacaofinalizada.GerarMemorandoMovimentacaoInternaFinalizadaUseCase;
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
@RequestMapping("/movimentacao/{id}/memorando/finalizado")
public class GerarMemorandoMovimentacaoInternaFinalizadaController {

    @Autowired
    private GerarMemorandoMovimentacaoInternaFinalizadaUseCase useCase;

    @GetMapping
    public void executar(@PathVariable Long id, HttpServletResponse httpServletResponse) throws IOException {
        GerarMemorandoMovimentacaoInternaFinalizadaOutputData outputData = useCase.executar(new GerarMemorandoMovimentacaoInternaFinalizadaInputData(id));

        prepareDownloadResponse(outputData,httpServletResponse);
    }

    private void prepareDownloadResponse(GerarMemorandoMovimentacaoInternaFinalizadaOutputData arquivo, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType(arquivo.getContentType());
        httpServletResponse.setHeader("content-disposition", "attachment; filename=\"" + arquivo.getNome() + "\"");

        ByteArrayInputStream byt = new ByteArrayInputStream(arquivo.getContent());

        IOUtils.copy(byt, httpServletResponse.getOutputStream());
        httpServletResponse.flushBuffer();
    }
}
