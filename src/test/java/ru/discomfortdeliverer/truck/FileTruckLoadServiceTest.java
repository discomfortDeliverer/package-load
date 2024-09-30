package ru.discomfortdeliverer.truck;

import org.junit.jupiter.api.Test;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.model.truck.Truck;
import ru.discomfortdeliverer.service.truck.FileTruckLoadService;

import static org.assertj.core.api.Assertions.assertThat;

public class FileTruckLoadServiceTest {
    private FileTruckLoadService fileTruckLoadService;
    private final String ls = System.lineSeparator();

    @Test
    void loadTruckFromJsonFile_GivenPathToJsonFile_ShouldReturnTruck() {
        fileTruckLoadService = new FileTruckLoadService();
        Truck loadedTruckFromJsonFile = fileTruckLoadService.loadTruckFromJsonFile("src/test/resources/trucks/test-truck.json");
        Truck expectedTruck = new Truck();
        expectedTruck.placeParcelByCoordinates(new Parcel("999" + ls + "999" + ls + "999"), 0, 0);
        expectedTruck.placeParcelByCoordinates(new Parcel("8888" + ls + "8888"), 3, 0);

        assertThat(loadedTruckFromJsonFile).isEqualTo(expectedTruck);
    }
}
