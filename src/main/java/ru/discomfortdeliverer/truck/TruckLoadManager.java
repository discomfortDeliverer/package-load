package ru.discomfortdeliverer.truck;

import lombok.extern.slf4j.Slf4j;
import ru.discomfortdeliverer.Coordinates;
import ru.discomfortdeliverer.parcel.Parcel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class TruckLoadManager {
    private void sortByParcelArea(List<Parcel> parcels) {
        parcels.sort((p1, p2) -> Integer.compare(p2.getArea(), p1.getArea()));
    }

    public List<Truck> oneParcelOneTruckLoad(List<Parcel> parcels) {
        log.info("Метод oneParcelOneTruckLoad, добавляем список посылок, размером - {}", parcels.size());
        List<Truck> trucks = new ArrayList<>();
        sortByParcelArea(parcels);

        for (Parcel parcel : parcels) {
            Truck truck = new Truck();
            truck.placeParcelByCoordinates(parcel, 0, 0);
            trucks.add(truck);
        }
        log.info("Получили список грузовиков размером - {}", trucks.size());
        return trucks;
    }

    public List<Truck> optimalLoading(List<Parcel> parcels) {
        log.info("Метод optimalLoading, добавляем список посылок, размером - {}", parcels.size());
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
        log.info("Получили список грузовиков размером - {}", trucks.size());
        return trucks;
    }
}
