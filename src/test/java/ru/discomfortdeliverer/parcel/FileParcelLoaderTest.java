package ru.discomfortdeliverer.parcel;

import org.junit.jupiter.api.Test;
import ru.discomfortdeliverer.exception.InvalidFilePathException;
import ru.discomfortdeliverer.exception.InvalidInputException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FileParcelLoaderTest {
    private FileParcelLoader fileParcelLoader;

    @Test
    void loadParcelsFromFile_GivenValidFile_ShouldReturnValidParcels() throws InvalidInputException, InvalidFilePathException {
        fileParcelLoader = new FileParcelLoader(new ParcelInputValidator());
        List<Parcel> parcelsFromFile = fileParcelLoader.loadParcelsFromFile("src/test/resources/validInputParcelsData.txt");

        List<Parcel> expectedParcels = new ArrayList<>();
        expectedParcels.add(new Parcel("999\n999\n999"));
        expectedParcels.add(new Parcel("8888\n8888"));
        expectedParcels.add(new Parcel("777\n7777"));
        expectedParcels.add(new Parcel("666\n666"));
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
