package ru.discomfortdeliverer.service.truck;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.discomfortdeliverer.model.parcel.Coordinates;
import ru.discomfortdeliverer.exception.UnableToLoadException;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.model.truck.Truck;

import java.util.*;

@Slf4j
@Service
public class ParcelLoadInTruckService {
    private static final int MAX_TRUCK_AREA = 36;
    private void sortByParcelArea(List<Parcel> parcels) {
        parcels.sort((p1, p2) -> Integer.compare(p2.getArea(), p1.getArea()));
    }

    /**
     * Режим работы погрузчика посылок, когда одна посылка загружается в один грузовик
     *
     * @param parcels Список посылок, которые нужно загрузить
     * @return Список с грузовиками, в каждом из которых погружена одна посылка
     */
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

    /**
     * Режим работы погрузчика посылок, когда посылки загружаются максимально плотно в
     * минимальное количество грузовиков
     *
     * @param parcels Список с посылками, которые надо загрузить
     * @return Список с грузовиками, в которых максимально плотно загружены посылки
     */
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

    /**
     * Режим работы погрузчика, в котором загружаем посылки в указанное количество грузовиков
     * максимально компактным способом
     *
     * @param parcels Список с посылками, которые нужно загрузить
     * @param trucksCount Количество грузовиков, в которые нуобходимо погрузить посылки
     * @return Список грузовиков с загруженными посылками, максимально плотным образом
     * @throws UnableToLoadException Выбрасывается, если не удается погрузить посылки в
     * указанное число грузовиков
     */
    public List<Truck> maxQualityLoad(List<Parcel> parcels, int trucksCount) throws UnableToLoadException {
        int allParcelsArea = 0;
        for (Parcel parcel : parcels) {
            allParcelsArea += parcel.getArea();
        }

        if (allParcelsArea > trucksCount * MAX_TRUCK_AREA) {
            throw new UnableToLoadException("Посылки займут места больше чем есть места в " + trucksCount + " грузовиках");
        }

        List<Truck> trucks = optimalLoading(parcels);
        if (trucks.size() > trucksCount) {
            throw new UnableToLoadException("Посылки не могут поместиться в " + trucksCount + " грузовиков");
        }
        return trucks;
    }

    /**
     * Режим работы погрузчика, в котором посылки помещаются в указанное количество грузовиков равномерно
     *
     * Равномерно - в каждом грузовике будет заполнено примерно одинаковое количество ячеек
     * @param parcels Список с посылками
     * @param trucksCount Количество грузовиков, в которое надо поместить посылки
     * @return Список с грузовиками, в которых посылки погружены равномерно
     * @throws UnableToLoadException Выбрасывается в случае, если нельзя поместить посылки в указанное
     * количество грузовиков
     */
    public List<Truck> evenLoad(List<Parcel> parcels, int trucksCount) throws UnableToLoadException {
        sortByParcelArea(parcels);
        List<List<Parcel>> trucksAndParcels = new ArrayList<>();

        for (int i = 0; i < trucksCount; i++) {
            trucksAndParcels.add(new ArrayList<>());
        }

        int left = 0;
        int right = parcels.size() - 1;
        while (left < right) {
            for (int i=0; i<trucksCount; i++) {
                ArrayList<Parcel> currentParcels = (ArrayList<Parcel>) trucksAndParcels.get(i);
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

    private List<Truck> loadTrucksFromParcelLists(List<List<Parcel>> trucksAndParcels) throws UnableToLoadException {
        List<Truck> trucks = new ArrayList<>();

        for (List<Parcel> list : trucksAndParcels) {
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

    public List<Truck> loadParcelsToTrucks(List<Parcel> parcels, String trucksSize) {
        List<Truck> trucks = createTrucksFromSizes(trucksSize);

        sortByParcelArea(parcels);

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
                throw new UnableToLoadException("Невозможно поместить посылку -" + parcel +
                        " ни в один из грузовиков, размерами: "  + trucksSize);
            }
        }
        log.info("Получили список грузовиков размером - {}", trucks.size());
        return trucks;
    }

    private List<Truck> createTrucksFromSizes(String trucksSize) {
        String[] sizes = trucksSize.split(",");

        List<Truck> trucks = new ArrayList<>();
        for (String size : sizes) {
            String[] heightAndLength = size.split("x");
            int height = Integer.parseInt(heightAndLength[0]);
            int length = Integer.parseInt(heightAndLength[1]);
            Truck truck = new Truck(height, length);
            trucks.add(truck);
        }
        return trucks;
    }
}
