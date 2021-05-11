package br.com.azi.patrimoniomobiliario.domain.entity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Calendario {
    List<LocalDate> feriados;

    public Calendario(List<String> feriados) {
        this.feriados = formatarFeriadosEmDatas(feriados);
    }

    public boolean naoEDiaUtil(LocalDateTime input) {
        return this.ehFeriado(input) || this.ehFinalDeSemana(input);
    }

    private boolean ehFeriado(LocalDateTime input) {
        LocalDate data = input.toLocalDate();
        if (Objects.isNull(feriados) || feriados.isEmpty()) {
            return false;
        }
        return feriados.stream().anyMatch(feriado -> feriado.equals(data));
    }

    private boolean ehFinalDeSemana(LocalDateTime input) {
        LocalDate data = input.toLocalDate();
        return DayOfWeek.SATURDAY.equals(data.getDayOfWeek()) || DayOfWeek.SUNDAY.equals(data.getDayOfWeek());
    }

    private List<LocalDate> formatarFeriadosEmDatas(List<String> feriados) {
        if (Objects.isNull(feriados) || feriados.isEmpty()) {
            return Collections.emptyList();
        }
        feriados = feriados.stream().filter(feriado -> !feriado.isEmpty()).collect(Collectors.toList());
        return feriados.stream().map(this::formatarData).collect(Collectors.toList());
    }

    private LocalDate formatarData(String feriado) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String feriadoComAno = feriado.concat("/").concat(String.valueOf(LocalDate.now().getYear()));
        return LocalDate.parse(feriadoComAno, formatter);
    }
}
