package ru.discomfortdeliverer.truck;

import ru.discomfortdeliverer.Coordinates;
import ru.discomfortdeliverer.parcel.Parcel;

import java.util.*;

public class TruckLoadManager {
    private void sortByParcelArea(List<Parcel> parcels) {
        parcels.sort((p1, p2) -> Integer.compare(p2.getArea(), p1.getArea()));
    }
    public List<Truck> oneParcelOneTruckLoad(List<Parcel> parcels) {
        List<Truck> trucks = new ArrayList<>();
        sortByParcelArea(parcels);

        for (Parcel parcel : parcels) {
            Truck truck = new Truck();
            truck.placeParcelByCoordinates(parcel, 0, 0);
            trucks.add(truck);
        }
        return trucks;
    }

    public List<Truck> optimalLoading(List<Parcel> parcels) {
        sortByParcelArea(parcels);

        List<Truck> trucks = new ArrayList<>();
        trucks.add(new Truck());

        for (Parcel parcel : parcels) {
            boolean placed = false;

            for (Truck truck : trucks) {
                Optional<Coordinates> coordinatesToPlace = truck.findCoordinatesToPlace(parcel);
                if (coordinatesToPlace.isPresent()) {
                    Coordinates coordinates = coordinatesToPlace.get();
                    truck.placeParcelByCoordinates(parcel, coordinates.getRow(), coordinates.getCol());
                    placed = true;
                    break;
                }
            }

            if (!placed) {
                Truck newTruck = new Truck();
                newTruck.placeParcelByCoordinates(parcel, 0, 0);
                trucks.add(newTruck);
            }
        }
        return trucks;
    }
}
