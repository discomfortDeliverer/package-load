package ru.discomfortdeliverer.truck;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TruckUtilsTest {
    private TruckUtils truckUtils;
    private FileTruckLoader fileTruckLoader;

    @Test
    void countEachTypeParcels_GivenJsonTruck_ShouldReturnCorrectCount() {
        truckUtils = new TruckUtils();
        fileTruckLoader = new FileTruckLoader();

        Truck truck = fileTruckLoader.loadTruckFromJsonFile("src/test/resources/trucks/test-parcels-count-in-truck.json");
        Map<String, Integer> parcelsCounts = truckUtils.countEachTypeParcels(truck);

        Map<String, Integer> expectedResult = new HashMap<>();
        expectedResult.put("9", 1);
        expectedResult.put("8", 1);
        expectedResult.put("5", 1);
        expectedResult.put("3", 2);
        expectedResult.put("2", 2);
        expectedResult.put("1", 4);
        assertThat(parcelsCounts).isEqualTo(expectedResult);
    }
}
