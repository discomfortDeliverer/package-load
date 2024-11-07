package ru.discomfortdeliverer.utils;

import org.springframework.stereotype.Service;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.model.truck.Truck;

import java.util.List;

@Service
public class ParcelLoaderUtils {
    public void sortByParcelArea(List<Parcel> parcels) {
        parcels.sort((p1, p2) -> Integer.compare(p2.getArea(), p1.getArea()));
    }

    public void reverseTrucksBody(List<Truck> trucks) {
        for (Truck truck : trucks) {
            truck.reverseTruckBody();
        }
    }
}
