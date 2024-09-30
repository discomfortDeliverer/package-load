package ru.discomfortdeliverer.parcel;

import org.junit.jupiter.api.Test;
import ru.discomfortdeliverer.model.parcel.Parcel;

import static org.assertj.core.api.Assertions.assertThat;

public class ParcelTest {
    private Parcel parcel;
    private final String ls = System.lineSeparator();

    @Test
    void constructorTest() {
        String inputFromFile = "777" + ls +
                               "7777" + ls;
        parcel = new Parcel(inputFromFile);
        assertThat(parcel.getHeight()).isEqualTo(2);
        assertThat(parcel.getLength()).isEqualTo(4);

        char[][] expectedShape = {
                {'7', '7', '7', '7'},  // 0 индекс
                {'7', '7', '7', ' '}   // 1 индекс
        };
        char[][] shape = parcel.getForm();
        assertThat(shape).isEqualTo(expectedShape);
    }
}
