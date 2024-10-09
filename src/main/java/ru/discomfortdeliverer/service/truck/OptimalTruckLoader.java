package ru.discomfortdeliverer.service.truck;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.discomfortdeliverer.exception.UnableToLoadException;
import ru.discomfortdeliverer.model.parcel.Coordinates;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.model.truck.Truck;
import ru.discomfortdeliverer.utils.ParcelLoaderUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OptimalTruckLoader {

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
            log.error("Невозможно погрузить посылки - {} в {} грузовиков размером - {}",
                    parcels, maxTruckCount, truckSize);
            throw new UnableToLoadException("Невозможно погрузить посылки - " + parcels +
                    " в " + maxTruckCount + " грузовиков размером - " + truckSize);
        } else {
            parcelLoaderUtils.reverseTrucksBody(trucks);
            return trucks;
        }
    }
}
