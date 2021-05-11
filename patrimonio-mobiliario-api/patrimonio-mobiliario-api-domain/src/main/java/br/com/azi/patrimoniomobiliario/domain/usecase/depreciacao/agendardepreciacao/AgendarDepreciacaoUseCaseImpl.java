package br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.agendardepreciacao;

import br.com.azi.patrimoniomobiliario.domain.entity.Calendario;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeConfiguracoesIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.agendardepreciacao.converter.AgendarDepreciacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
public class AgendarDepreciacaoUseCaseImpl implements AgendarDepreciacaoUseCase {

    private final SistemaDeConfiguracoesIntegration sistemaDeConfiguracoesIntegration;

    private final AgendarDepreciacaoOutputDataConverter outputDataConverter;

    private final String diaMensalDepreciacao;

    private final List<String> feriadosNacionais;

    private final List<String> feriadosLocais;

    private final Clock clock;

    @Override
    public AgendarDepreciacaoOutputData executar() {
        LocalDate diaAgendado = encontrarDiaProximaExecucao();
        String cronExpression = converterDataEmExpressaoCron(diaAgendado);

        salvarExpressaoCronDepreciacao(cronExpression);

        return outputDataConverter.to(diaAgendado, cronExpression);
    }

    private LocalDate encontrarDiaProximaExecucao() {
        LocalDate dataAgendamento = LocalDate.of(LocalDate.now(clock).getYear(),LocalDate.now(clock).getMonthValue(),Integer.parseInt(diaMensalDepreciacao));
        int diaUtil = 0;
        int diasCorrridos = 1;
        while (verificaSeDiaAgendadoUtil(dataAgendamento)) {

            if (diaUtil < Integer.parseInt(diaMensalDepreciacao)) {
                diaUtil += 1;
            }
            dataAgendamento = DateUtils.somaDias(LocalDate.now(clock), diasCorrridos);
            diasCorrridos += 1;

        }
        return dataAgendamento;
    }

    private boolean verificaSeDiaAgendadoUtil(LocalDate data) {
        feriadosNacionais.addAll(feriadosLocais);
        Calendario calendario = new Calendario(feriadosNacionais);
        return calendario.naoEDiaUtil(data.atStartOfDay());
    }

    private String converterDataEmExpressaoCron(LocalDate data) {
        return String.format("0 5 0 %1$s %2$s ?", data.getDayOfMonth(), data.getMonthValue());
    }

    private void salvarExpressaoCronDepreciacao(String cronExpression) {
        sistemaDeConfiguracoesIntegration.alterarPropriedade("az.patrimonio-mobiliario.cron-agendamento-depreciacao", cronExpression);
    }
}
