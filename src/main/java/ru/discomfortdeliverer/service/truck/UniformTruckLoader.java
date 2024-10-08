package ru.discomfortdeliverer.service.truck;

import org.springframework.stereotype.Service;
import ru.discomfortdeliverer.exception.UnableToLoadException;
import ru.discomfortdeliverer.model.parcel.Coordinates;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.model.truck.Truck;

import java.util.ArrayList;
import java.util.List;

@Service
public class UniformTruckLoader {

    private void sortByParcelArea(List<Parcel> parcels) {
        parcels.sort((p1, p2) -> Integer.compare(p2.getArea(), p1.getArea()));
    }

    public List<Truck> loadParcels(List<Parcel> parcels, String truckSize, String maxTruckCount) {
        sortByParcelArea(parcels);

        List<Truck> trucks = new ArrayList<>();
        String[] heightAndLength = truckSize.trim().split("x");
        int trucksCounts = Integer.parseInt(maxTruckCount);

        for (int i = 0; i < trucksCounts; i++) {
            Truck truck = new Truck(Integer.parseInt(heightAndLength[0]),
                    Integer.parseInt(heightAndLength[1]));
            trucks.add(truck);
        }

        int currentTruckIndex = 0;
        for (Parcel parcel : parcels) {
            boolean isPlaced = false;

            for (int i = 0; i < trucks.size(); i++) {
                Truck truck = trucks.get(currentTruckIndex);
                currentTruckIndex = currentTruckIndex + 1;
                Coordinates coordinatesToPlace = truck.findCoordinatesToPlace(parcel);
                if (coordinatesToPlace != null) {
                    truck.placeParcelByCoordinates(parcel,
                            coordinatesToPlace.getRow(),
                            coordinatesToPlace.getCol());
                    isPlaced = true;
                    break;
                }
            }
            if (!isPlaced) {
                throw new UnableToLoadException("Невозможно погрузить посылки - " + parcels +
                        " в " + maxTruckCount + " грузовиков размером - " + truckSize);
            }
            if (currentTruckIndex >= trucks.size()) {
                currentTruckIndex = 0;
            }
        }

        reverseTrucksBody(trucks);
        return trucks;
    }

    private void reverseTrucksBody(List<Truck> trucks) {
        for (Truck truck : trucks) {
            truck.reverseTruckBody();
        }
    }
}
