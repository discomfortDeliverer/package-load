package ru.discomfortdeliverer.truck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TruckUtils {
    private static final int MAX_PARCEL_SIZE = 9;
    private static final int[] PARCEL_SIZES = {1, 2, 3, 4, 5, 6, 7, 8, 9};

    /**
     * Считает какие посылки погружены в грузовик и сколько посылок каждого типа
     * @param truck Грузовик, в котором нужно посчитать посылки
     * @return Объект типа TruckParcelsCounter в котором содержатся данные
     * о количестве каждого типа посылок
     */
    public TruckParcelsCounter countEachTypeParcels(Truck truck) {
        char[][] truckBody = truck.getTruckBody();

        int[] sizeCounts = new int[MAX_PARCEL_SIZE + 1];

        for (char[] row : truckBody) {
            for (char col : row) {
                int size = Character.getNumericValue(col);
                if (size >= 1 && size <= MAX_PARCEL_SIZE) {
                    sizeCounts[size]++;
                }
            }
        }

        TruckParcelsCounter truckParcelsCounter = new TruckParcelsCounter();
        for (int size : PARCEL_SIZES) {
            if (sizeCounts[size] > 0) {
                truckParcelsCounter.addParcelAndCount(String.valueOf(size), sizeCounts[size] / size);
            }
        }

        return truckParcelsCounter;
    }

    /**
     * Считает какие посылки погружены в каждый грузовик и сколько посылок каждого типа
     * @param trucks Список с грузовиками, в которых нужно посчитать посылки
     * @return Список с объектами типа TruckParcelsCounter в каждом объекте содержатся данные
     * о количестве посылок каждого типа, содержащегося в грузовике
     */
    public List<TruckParcelsCounter> countEachTypeParcelsFromTruckList(List<Truck> trucks) {
        List<TruckParcelsCounter> truckParcelsCounters = new ArrayList<>();
        for (Truck truck : trucks) {
            TruckParcelsCounter parcelsAndCount = countEachTypeParcels(truck);
            truckParcelsCounters.add(parcelsAndCount);
        }

        return truckParcelsCounters;
    }
}
