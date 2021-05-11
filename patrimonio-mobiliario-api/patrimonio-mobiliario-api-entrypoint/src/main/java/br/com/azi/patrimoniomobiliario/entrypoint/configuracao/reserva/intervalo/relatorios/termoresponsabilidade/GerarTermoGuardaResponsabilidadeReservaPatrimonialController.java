package br.com.azi.patrimoniomobiliario.entrypoint.configuracao.reserva.intervalo.relatorios.termoresponsabilidade;

import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade.GerarTermoGuardaResponsabilidadeReservaInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade.GerarTermoGuardaResponsabilidadeReservaOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reserva.intervalo.relatorios.termoresponsabilidade.GerarTermoGuardaResponsabilidadeReservaUseCase;
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
@RequestMapping("/configuracao/reservas/{reservaId}/intervalos/{intervaloId}/termoDeGuarda")
public class GerarTermoGuardaResponsabilidadeReservaPatrimonialController {

    @Autowired
    private GerarTermoGuardaResponsabilidadeReservaUseCase useCase;

    @GetMapping
    public void executar(@PathVariable("reservaId") Long reservaId,
                         @PathVariable("intervaloId") Long intervaloId,
                         HttpServletResponse httpServletResponse) throws IOException {
        GerarTermoGuardaResponsabilidadeReservaInputData inputData = new GerarTermoGuardaResponsabilidadeReservaInputData(reservaId, intervaloId);
        GerarTermoGuardaResponsabilidadeReservaOutputData outputData = useCase.executar(inputData);

        prepareDownloadResponse(outputData, httpServletResponse);
    }

    private void prepareDownloadResponse(GerarTermoGuardaResponsabilidadeReservaOutputData arquivo, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType(arquivo.getContentType());
        httpServletResponse.setHeader("content-disposition", "attachment; filename=\"" + arquivo.getNome() + "\"");

        ByteArrayInputStream byt = new ByteArrayInputStream(arquivo.getContent());

        IOUtils.copy(byt, httpServletResponse.getOutputStream());
        httpServletResponse.flushBuffer();
    }

}
