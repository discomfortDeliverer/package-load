package ru.discomfortdeliverer.truck;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TruckParcelsCounter {
    private final Map<String, Integer> parcelsAndCount;

    public TruckParcelsCounter() {
        this.parcelsAndCount = new HashMap<>();
    }

    public void addParcelAndCount(String parcel, int count) {
        parcelsAndCount.put(parcel, count);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TruckParcelsCounter that = (TruckParcelsCounter) o;
        return Objects.equals(parcelsAndCount, that.parcelsAndCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parcelsAndCount);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Integer> entry : parcelsAndCount.entrySet()) {
            stringBuilder.append("'")
                    .append(entry.getKey())
                    .append("'")
                    .append(" - ")
                    .append(entry.getValue())
                    .append("\n");
        }

        return stringBuilder.toString();
    }
}
