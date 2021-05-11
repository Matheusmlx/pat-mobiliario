package br.com.azi.patrimoniomobiliario.utils.converter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GenericConverterTest {

    private static final ModelConverter modelConverter = new ModelConverter();

    @Test
    public void shouldCopyEnumToStringCorrectly() {
        ModelA modelA = new ModelA(1L, "Object", Color.BLACK, 1, 1.0);
        ModelB destination = modelConverter.to(modelA);

        Assert.assertEquals(modelA.getId(), destination.getId());
        Assert.assertEquals(modelA.getDescription(), destination.getDescription());
        Assert.assertEquals(modelA.getColor().name(), destination.getColor());
        Assert.assertNotEquals(modelA.getOn(), destination.getOn());
        Assert.assertNull(destination.getOn());
    }

    @Test
    public void shouldCopyToStringAlthoughEnumNullable() {
        ModelA modelA = new ModelA(null, "Null", null, null, null);
        ModelB destination = modelConverter.to(modelA);

        Assert.assertNull(destination.getColor());
    }

    @Test
    public void shouldCopyStringToEnumCorrectly() {
        ModelB modelB = new ModelB(1L, "Object", "WHITE", Boolean.TRUE);
        ModelA destination = modelConverter.from(modelB);

        Assert.assertEquals(modelB.getId(), destination.getId());
        Assert.assertEquals(modelB.getDescription(), destination.getDescription());
        Assert.assertEquals(modelB.getColor(), destination.getColor().name());
        Assert.assertNull(destination.getOn());
    }

    @Test
    public void shouldCopyToEnumAlthoughStringNullable() {
        ModelB modelB = new ModelB(null, "Null", null, null);
        ModelA destination = modelConverter.from(modelB);

        Assert.assertNull(destination.getColor());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ModelA {
        private Long id;
        private String description;
        private Color color;
        private Integer on;
        private Double size;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ModelB {
        private Long id;
        private String description;
        private String color;
        private Boolean on;
    }
    
    public static class ModelConverter extends GenericConverter<ModelA, ModelB> {}

    public enum Color {
        BLACK, WHITE;
    }
}