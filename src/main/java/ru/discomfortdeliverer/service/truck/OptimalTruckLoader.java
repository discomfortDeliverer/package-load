package ru.discomfortdeliverer.service.truck;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.discomfortdeliverer.exception.UnableToLoadException;
import ru.discomfortdeliverer.model.parcel.Coordinates;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.model.truck.Truck;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OptimalTruckLoader {
    private void sortByParcelArea(List<Parcel> parcels) {
        parcels.sort((p1, p2) -> Integer.compare(p2.getArea(), p1.getArea()));
    }

    public List<Truck> loadParcels(List<Parcel> parcels, String truckSize, String maxTruckCount) {
        log.info("Метод loadParcels, добавляем список посылок, размером - {}", parcels.size());
        sortByParcelArea(parcels);

        List<Truck> trucks = new ArrayList<>();
        String[] heightAndLength = truckSize.trim().split("x");
        int trucksCounts = Integer.parseInt(maxTruckCount);

        for (int i = 0; i < trucksCounts; i++) {
            Truck truck = new Truck(Integer.parseInt(heightAndLength[0]),
                    Integer.parseInt(heightAndLength[1]));
            trucks.add(truck);
        }

        for (Truck truck : trucks) {
            List<Parcel> loadedParcels = new ArrayList<>();
            for (Parcel parcel : parcels) {
                Coordinates coordinatesToPlace = truck.findCoordinatesToPlace(parcel);
                if (coordinatesToPlace != null) {
                    truck.placeParcelByCoordinates(parcel,
                            coordinatesToPlace.getRow(),
                            coordinatesToPlace.getCol());
                    loadedParcels.add(parcel);
                }
            }

            // Удаляем уже добавленные посылки
            for (Parcel loadedParcel : loadedParcels) {
                parcels.remove(loadedParcel);
            }
        }

        if (!parcels.isEmpty()) {
            throw new UnableToLoadException("Невозможно погрузить посылки - " + parcels +
                    " в " + maxTruckCount + " грузовиков размером - " + truckSize);
        } else {
            reverseTrucksBody(trucks);
            return trucks;
        }
    }

    private void reverseTrucksBody(List<Truck> trucks) {
        for (Truck truck : trucks) {
            truck.reverseTruckBody();
        }
    }
}
