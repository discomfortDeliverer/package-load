package ru.discomfortdeliverer.truck;

import org.junit.jupiter.api.Test;
import ru.discomfortdeliverer.model.Parcel;

import static org.assertj.core.api.Assertions.assertThat;

public class FileTruckLoaderTest {
    private FileTruckLoader fileTruckLoader;
    private final String ls = System.lineSeparator();

    @Test
    void loadTruckFromJsonFile_GivenPathToJsonFile_ShouldReturnTruck() {
        fileTruckLoader = new FileTruckLoader();
        Truck loadedTruckFromJsonFile = fileTruckLoader.loadTruckFromJsonFile("src/test/resources/trucks/test-truck.json");
        Truck expectedTruck = new Truck();
        expectedTruck.placeParcelByCoordinates(new Parcel("999" + ls + "999" + ls + "999"), 0, 0);
        expectedTruck.placeParcelByCoordinates(new Parcel("8888" + ls + "8888"), 3, 0);

        assertThat(loadedTruckFromJsonFile).isEqualTo(expectedTruck);
    }
}
