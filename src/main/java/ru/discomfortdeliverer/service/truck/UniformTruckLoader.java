package ru.discomfortdeliverer.service.truck;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.discomfortdeliverer.exception.UnableToLoadException;
import ru.discomfortdeliverer.model.parcel.Coordinates;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.model.truck.Truck;
import ru.discomfortdeliverer.utils.ParcelLoaderUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UniformTruckLoader {

    private final ParcelLoaderUtils parcelLoaderUtils;

    public List<Truck> loadParcels(List<Parcel> parcels, String truckSize, String maxTruckCount) {
        parcelLoaderUtils.sortByParcelArea(parcels);

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

        parcelLoaderUtils.reverseTrucksBody(trucks);
        return trucks;
    }
}
