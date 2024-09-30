package ru.discomfortdeliverer.truck;

import org.junit.jupiter.api.Test;
import ru.discomfortdeliverer.model.truck.Truck;
import ru.discomfortdeliverer.service.truck.FileTruckLoadService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TruckUtilsTest {
    private TruckUtils truckUtils;
    private FileTruckLoadService fileTruckLoadService;

    @Test
    void countEachTypeParcels_GivenJsonTruck_ShouldReturnCorrectCount() {
        truckUtils = new TruckUtils();
        fileTruckLoadService = new FileTruckLoadService();

        Truck truck = fileTruckLoadService.loadTruckFromJsonFile("src/test/resources/trucks/test-parcels-count-in-truck.json");
        TruckParcelsCounter parcelsCounts = truckUtils.countEachTypeParcels(truck);

        TruckParcelsCounter expectedResult = new TruckParcelsCounter();
        expectedResult.addParcelAndCount("9", 1);
        expectedResult.addParcelAndCount("8", 1);
        expectedResult.addParcelAndCount("5", 1);
        expectedResult.addParcelAndCount("3", 2);
        expectedResult.addParcelAndCount("2", 2);
        expectedResult.addParcelAndCount("1", 4);
        assertThat(parcelsCounts).isEqualTo(expectedResult);
    }

    @Test
    void countEachTypeParcelsFromTruckList_ShouldReturnCorrectCount() {
        truckUtils = new TruckUtils();
        fileTruckLoadService = new FileTruckLoadService();

        List<Truck> trucks = fileTruckLoadService.loadTrucksFromJsonFile("src/test/resources/trucks/test-many-trucks.json");
        List<TruckParcelsCounter> truckParcelsCounters = truckUtils.countEachTypeParcelsFromTruckList(trucks);

        List<TruckParcelsCounter> expectedTruckParcelsCounters = new ArrayList<>();

        TruckParcelsCounter truckParcelsCounter = new TruckParcelsCounter();
        truckParcelsCounter.addParcelAndCount("9", 1);
        truckParcelsCounter.addParcelAndCount("1", 5);
        truckParcelsCounter.addParcelAndCount("2", 1);
        truckParcelsCounter.addParcelAndCount("3", 1);
        truckParcelsCounter.addParcelAndCount("7", 1);
        expectedTruckParcelsCounters.add(truckParcelsCounter);

        truckParcelsCounter = new TruckParcelsCounter();
        truckParcelsCounter.addParcelAndCount("1", 1);
        truckParcelsCounter.addParcelAndCount("2", 3);
        truckParcelsCounter.addParcelAndCount("5", 1);
        truckParcelsCounter.addParcelAndCount("7", 1);
        truckParcelsCounter.addParcelAndCount("8", 1);
        expectedTruckParcelsCounters.add(truckParcelsCounter);

        truckParcelsCounter = new TruckParcelsCounter();
        truckParcelsCounter.addParcelAndCount("1", 9);
        truckParcelsCounter.addParcelAndCount("2", 1);
        truckParcelsCounter.addParcelAndCount("7", 1);
        truckParcelsCounter.addParcelAndCount("9", 1);
        expectedTruckParcelsCounters.add(truckParcelsCounter);

        truckParcelsCounter = new TruckParcelsCounter();
        truckParcelsCounter.addParcelAndCount("1", 4);
        truckParcelsCounter.addParcelAndCount("2", 1);
        truckParcelsCounter.addParcelAndCount("3", 2);
        truckParcelsCounter.addParcelAndCount("4", 1);
        truckParcelsCounter.addParcelAndCount("6", 1);
        truckParcelsCounter.addParcelAndCount("7", 1);
        expectedTruckParcelsCounters.add(truckParcelsCounter);

        assertThat(truckParcelsCounters.size()).isEqualTo(expectedTruckParcelsCounters.size());
        assertThat(truckParcelsCounters).isEqualTo(expectedTruckParcelsCounters);
    }
}
