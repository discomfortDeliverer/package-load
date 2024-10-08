package ru.discomfortdeliverer.service.truck;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.model.truck.Truck;
import ru.discomfortdeliverer.service.parcel.ParcelService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleTruckLoader {

    private final ParcelService parcelService;

    private void sortByParcelArea(List<Parcel> parcels) {
        parcels.sort((p1, p2) -> Integer.compare(p2.getArea(), p1.getArea()));
    }

    public List<Truck> loadParcels(List<Parcel> parcels) {
        log.info("Метод oneParcelOneTruckLoad, добавляем список посылок, размером - {}", parcels.size());
        List<Truck> trucks = new ArrayList<>();
        sortByParcelArea(parcels);

        for (Parcel parcel : parcels) {
            Truck truck = new Truck(parcel.getHeight(), parcel.getLength());
            truck.placeParcelByCoordinates(parcel, 0, 0);
            trucks.add(truck);
        }
        log.info("Получили список грузовиков размером - {}", trucks.size());
        reverseTrucksBody(trucks);
        return trucks;
    }

    private void reverseTrucksBody(List<Truck> trucks) {
        for (Truck truck : trucks) {
            truck.reverseTruckBody();
        }
    }
}
