package ru.discomfortdeliverer.model.truck;

import java.util.ArrayList;
import java.util.List;

public class TruckParcelsCounterWrapper {
    private final List<TruckParcelsCounter> truckParcelsCounters;

    public TruckParcelsCounterWrapper() {
        this.truckParcelsCounters = new ArrayList<>();
    }

    public void addTruckParcelsCounter(TruckParcelsCounter truckParcelsCounter) {
        truckParcelsCounters.add(truckParcelsCounter);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < truckParcelsCounters.size(); i++) {
            stringBuilder.append("Грузовик #" + (i + 1));
            stringBuilder.append(truckParcelsCounters.get(i));
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}
