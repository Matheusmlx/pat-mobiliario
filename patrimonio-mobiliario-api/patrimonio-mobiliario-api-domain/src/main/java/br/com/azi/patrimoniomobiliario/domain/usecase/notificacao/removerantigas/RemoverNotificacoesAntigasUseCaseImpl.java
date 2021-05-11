package br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.removerantigas;

import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotificacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.removerantigas.converter.RemoverNotificacoesAntigasOutputDataConverter;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
public class RemoverNotificacoesAntigasUseCaseImpl implements RemoverNotificacoesAntigasUseCase {

    private static final int DIAS_LIMITE = 30;

    private final Clock clock;

    private final NotificacaoDataProvider notificacaoDataProvider;

    private final RemoverNotificacoesAntigasOutputDataConverter converter;

    @Override
    public RemoverNotificacoesAntigasOutputData executar() {
        final Date dataReferencia = DateUtils.asDate(LocalDateTime.now(clock).minusDays(DIAS_LIMITE));

        final int quantidadeRemovidas = notificacaoDataProvider.removerNotificacoesAntigas(dataReferencia);

        return converter.to(dataReferencia, quantidadeRemovidas);
    }
}
