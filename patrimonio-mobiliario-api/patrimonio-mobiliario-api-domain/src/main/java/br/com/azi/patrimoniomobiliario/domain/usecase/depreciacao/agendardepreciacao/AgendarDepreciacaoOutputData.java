package br.com.azi.patrimoniomobiliario.domain.usecase.depreciacao.agendardepreciacao;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class AgendarDepreciacaoOutputData {
    private LocalDate data;
    private String cron;
}
