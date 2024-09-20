package ru.discomfortdeliverer.truck;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.discomfortdeliverer.exception.UnableToLoadException;
import ru.discomfortdeliverer.parcel.FileParcelLoader;
import ru.discomfortdeliverer.parcel.Parcel;
import ru.discomfortdeliverer.view.ConsoleTruckView;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TruckLoadManagerTest {
    private TruckLoadManager truckLoadManager;
    private List<Parcel> parcels;

    @BeforeEach
    void setUp() {
        truckLoadManager = new TruckLoadManager();
    }

    @Test
    void oneParcelOneTruckLoad_ShouldReturnListOfTrucksWithOneParcelEach() {
        parcels = new ArrayList<>();
        parcels.add(new Parcel("1"));
        parcels.add(new Parcel("22"));
        parcels.add(new Parcel("333"));
        parcels.add(new Parcel("4444"));
        parcels.add(new Parcel("55555"));
        parcels.add(new Parcel("666\n666"));
        parcels.add(new Parcel("777\n7777"));
        parcels.add(new Parcel("8888\n8888"));
        parcels.add(new Parcel("999\n999\n999"));

        List<Truck> trucks = truckLoadManager.oneParcelOneTruckLoad(parcels);
        ConsoleTruckView.printListOfTrucks(trucks);
        assertThat(trucks.size()).isEqualTo(9);
    }

    @Test
    void optimalLoading_ShouldReturnListOfTrucksWhichMostTightlyPacked(){
        parcels = new ArrayList<>();
        parcels.add(new Parcel("1"));
        parcels.add(new Parcel("22"));
        parcels.add(new Parcel("333"));
        parcels.add(new Parcel("4444"));
        parcels.add(new Parcel("55555"));
        parcels.add(new Parcel("666\n666"));
        parcels.add(new Parcel("777\n7777"));
        parcels.add(new Parcel("8888\n8888"));
        parcels.add(new Parcel("999\n999\n999"));

        char[][] expectedFirstTruckBody = {
                {'9', '9', '9', '6', '6', '6'},
                {'9', '9', '9', '6', '6', '6'},
                {'9', '9', '9', '3', '3', '3'},
                {'8', '8', '8', '8', '2', '2'},
                {'8', '8', '8', '8', '1', ' '},
                {'5', '5', '5', '5', '5', ' '}
        };
        char[][] expectedSecondTruckBody = {
                {'7', '7', '7', '7', ' ', ' '},
                {'7', '7', '7', ' ', ' ', ' '},
                {'4', '4', '4', '4', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' '},
                {' ', ' ', ' ', ' ', ' ', ' '}
        };

        List<Truck> trucks = truckLoadManager.optimalLoading(parcels);

        ConsoleTruckView.printListOfTrucks(trucks);
        assertThat(trucks.get(0).getTruckBody()).isEqualTo(expectedFirstTruckBody);
        assertThat(trucks.get(1).getTruckBody()).isEqualTo(expectedSecondTruckBody);
    }

    @Test
    void evenLoad_GivenParcelsListAndTrucksCount_ShouldReturnEvenLoadedParcelsInTrucks() throws UnableToLoadException {
        parcels = new ArrayList<>();
        parcels.add(new Parcel("999\n999\n999"));
        parcels.add(new Parcel("999\n999\n999"));
        parcels.add(new Parcel("999\n999\n999"));
        parcels.add(new Parcel("999\n999\n999"));
        parcels.add(new Parcel("777\n7777"));
        parcels.add(new Parcel("777\n7777"));
        parcels.add(new Parcel("55555"));
        parcels.add(new Parcel("55555"));
        parcels.add(new Parcel("22"));
        parcels.add(new Parcel("22"));
        parcels.add(new Parcel("22"));
        parcels.add(new Parcel("1"));


        List<Truck> trucks = truckLoadManager.evenLoad(parcels, 3);
        assertThat(trucks.size()).isEqualTo(3);
    }

    @Test
    void evenLoad_GivenParcelsListAndWrongTrucksCount_ShouldThrowException() throws UnableToLoadException {
        parcels = new ArrayList<>();
        parcels.add(new Parcel("999\n999\n999"));
        parcels.add(new Parcel("999\n999\n999"));
        parcels.add(new Parcel("999\n999\n999"));
        parcels.add(new Parcel("999\n999\n999"));
        parcels.add(new Parcel("777\n7777"));
        parcels.add(new Parcel("777\n7777"));
        parcels.add(new Parcel("55555"));
        parcels.add(new Parcel("55555"));
        parcels.add(new Parcel("22"));
        parcels.add(new Parcel("22"));
        parcels.add(new Parcel("22"));
        parcels.add(new Parcel("1"));

        assertThatThrownBy(() -> {
            truckLoadManager.evenLoad(parcels, 1);
        })
                .isInstanceOf(UnableToLoadException.class);
    }
}