package br.com.azi.patrimoniomobiliario.utils.date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class DateUtilsTest {

    @Test
    public void deveCalcularDiferencaEmMesesEntreDatas() {

        assertEquals( 3 ,DateUtils.totalMeses(LocalDate.of(2020, 6, 10), LocalDate.of(2020, 10, 8)));

        assertEquals( 3 ,DateUtils.totalMeses(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 3, 24)));



    }
}
