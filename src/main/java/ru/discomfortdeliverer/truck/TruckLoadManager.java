package ru.discomfortdeliverer.truck;

import lombok.extern.slf4j.Slf4j;
import ru.discomfortdeliverer.Coordinates;
import ru.discomfortdeliverer.exception.UnableToLoadException;
import ru.discomfortdeliverer.parcel.Parcel;

import java.util.*;

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

    public List<Truck> maxQualityLoad(List<Parcel> parcels, int trucksCount) throws UnableToLoadException {
        int allParcelsArea = 0;
        for (Parcel parcel : parcels) {
            allParcelsArea += parcel.getArea();
        }

        if (allParcelsArea > trucksCount * 36) {
            throw new UnableToLoadException("Посылки займут места больше чем есть места в " + trucksCount + " грузовиках");
        }

        List<Truck> trucks = optimalLoading(parcels);
        if (trucks.size() > trucksCount) {
            throw new UnableToLoadException("Посылки не могут поместиться в " + trucksCount + " грузовиков");
        }
        return trucks;
    }

    public List<Truck> evenLoad(List<Parcel> parcels, int trucksCount) throws UnableToLoadException {
        sortByParcelArea(parcels);
        List<ArrayList<Parcel>> trucksAndParcels = new ArrayList<>();

        for (int i = 0; i < trucksCount; i++) {
            trucksAndParcels.add(new ArrayList<>());
        }

        int left = 0;
        int right = parcels.size() - 1;
        while (left < right) {
            for (int i=0; i<trucksCount; i++) {
                ArrayList<Parcel> currentParcels = trucksAndParcels.get(i);
                if (left == right) {
                    Parcel leftParcel = parcels.get(left);
                    currentParcels.add(leftParcel);
                    break;
                }
                if (left > right) {
                    break;
                }
                Parcel leftParcel = parcels.get(left);
                Parcel rigthParcel = parcels.get(right);

                currentParcels.add(leftParcel);
                currentParcels.add(rigthParcel);

                left++;
                right--;
            }
        }
        return loadTrucksFromParcelLists(trucksAndParcels);
    }

    private List<Truck> loadTrucksFromParcelLists(List<ArrayList<Parcel>> trucksAndParcels) throws UnableToLoadException {
        List<Truck> trucks = new ArrayList<>();

        for (ArrayList<Parcel> list : trucksAndParcels) {
            Truck truck = new Truck();
            for (Parcel parcel : list) {
                Optional<Coordinates> coordinatesToPlace = truck.findCoordinatesToPlace(parcel);
                if (coordinatesToPlace.isPresent()) {
                    Coordinates coordinates = coordinatesToPlace.get();
                    truck.placeParcelByCoordinates(parcel, coordinates.getRow(), coordinates.getCol());
                } else {
                    throw new UnableToLoadException("Посылка не помещается в грузовик");
                }
            }
            trucks.add(truck);
        }

        return trucks;
    }
}
