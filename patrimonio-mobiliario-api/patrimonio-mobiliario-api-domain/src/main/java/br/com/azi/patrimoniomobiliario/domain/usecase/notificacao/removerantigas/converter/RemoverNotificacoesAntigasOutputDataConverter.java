package br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.removerantigas.converter;

import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.removerantigas.RemoverNotificacoesAntigasOutputData;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RemoverNotificacoesAntigasOutputDataConverter {

    public RemoverNotificacoesAntigasOutputData to(Date dataReferencia, int quantidadeRemovidas) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        return new RemoverNotificacoesAntigasOutputData(
            simpleDateFormat.format(dataReferencia),
            quantidadeRemovidas
        );
    }

}
