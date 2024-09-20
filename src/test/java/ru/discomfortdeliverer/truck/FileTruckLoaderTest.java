package ru.discomfortdeliverer.truck;

import org.junit.jupiter.api.Test;
import ru.discomfortdeliverer.parcel.Parcel;

import static org.assertj.core.api.Assertions.assertThat;

public class FileTruckLoaderTest {
    private FileTruckLoader fileTruckLoader;

    @Test
    void loadTruckFromJsonFile_GivenPathToJsonFile_ShouldReturnTruck() {
        fileTruckLoader = new FileTruckLoader();
        Truck loadedTruckFromJsonFile = fileTruckLoader.loadTruckFromJsonFile("src/test/resources/trucks/test-truck.json");
        Truck expectedTruck = new Truck();
        expectedTruck.placeParcelByCoordinates(new Parcel("999\n999\n999"), 0, 0);
        expectedTruck.placeParcelByCoordinates(new Parcel("8888\n8888"), 3, 0);

        assertThat(loadedTruckFromJsonFile).isEqualTo(expectedTruck);
    }
}
