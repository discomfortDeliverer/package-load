package ru.discomfortdeliverer.parcel;

import org.junit.jupiter.api.Test;
import ru.discomfortdeliverer.exception.InvalidFilePathException;
import ru.discomfortdeliverer.exception.InvalidInputException;
import ru.discomfortdeliverer.model.Parcel;
import ru.discomfortdeliverer.service.parcel.FileParcelLoadService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FileParcelLoadServiceTest {
    private FileParcelLoadService fileParcelLoadService;
    private final String ls = System.lineSeparator();

    @Test
    void loadParcelsFromFile_GivenValidFile_ShouldReturnValidParcels() throws InvalidInputException, InvalidFilePathException {
        fileParcelLoadService = new FileParcelLoadService(new ParcelInputValidator());
        List<Parcel> parcelsFromFile = fileParcelLoadService.loadParcelsFromFile("src/test/resources/validInputParcelsData.txt");

        List<Parcel> expectedParcels = new ArrayList<>();
        expectedParcels.add(new Parcel("999" + ls + "999" + ls +"999"));
        expectedParcels.add(new Parcel("8888" + ls + "8888"));
        expectedParcels.add(new Parcel("777" + ls + "7777"));
        expectedParcels.add(new Parcel("666" + ls +"666"));
        expectedParcels.add(new Parcel("55555"));
        expectedParcels.add(new Parcel("4444"));
        expectedParcels.add(new Parcel("333"));
        expectedParcels.add(new Parcel("22"));
        expectedParcels.add(new Parcel("1"));

        assertThat(parcelsFromFile.get(0).equals(expectedParcels.get(0))).isTrue();
        assertThat(parcelsFromFile.get(1).equals(expectedParcels.get(1))).isTrue();
        assertThat(parcelsFromFile.get(2).equals(expectedParcels.get(2))).isTrue();
        assertThat(parcelsFromFile.get(3).equals(expectedParcels.get(3))).isTrue();
        assertThat(parcelsFromFile.get(4).equals(expectedParcels.get(4))).isTrue();
        assertThat(parcelsFromFile.get(5).equals(expectedParcels.get(5))).isTrue();
        assertThat(parcelsFromFile.get(6).equals(expectedParcels.get(6))).isTrue();
        assertThat(parcelsFromFile.get(7).equals(expectedParcels.get(7))).isTrue();
        assertThat(parcelsFromFile.get(8).equals(expectedParcels.get(8))).isTrue();
    }
}
