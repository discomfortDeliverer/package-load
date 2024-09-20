package ru.discomfortdeliverer.truck;

import java.util.HashMap;
import java.util.Map;

public class TruckUtils {
    private static final int MAX_PARCEL_SIZE = 9;
    private static final int[] PARCEL_SIZES = {1, 2, 3, 4, 5, 6, 7, 8, 9};

    public Map<String, Integer> countEachTypeParcels(Truck truck) {
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

        Map<String, Integer> parcelsAndCount = new HashMap<>();
        for (int size : PARCEL_SIZES) {
            if (sizeCounts[size] > 0) {
                parcelsAndCount.put(String.valueOf(size), sizeCounts[size] / size);
            }
        }

        return parcelsAndCount;
    }
}
