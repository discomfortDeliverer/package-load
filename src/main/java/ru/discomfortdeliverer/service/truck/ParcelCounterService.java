package ru.discomfortdeliverer.service.truck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.discomfortdeliverer.exception.UnknownPackageException;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.model.truck.Truck;
import ru.discomfortdeliverer.model.truck.TruckParcelsCounter;
import ru.discomfortdeliverer.model.truck.TruckParcelsCounterWrapper;
import ru.discomfortdeliverer.repository.ParcelRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParcelCounterService {
    private ParcelRepository parcelRepository;

    @Autowired
    public ParcelCounterService(ParcelRepository parcelRepository) {
        this.parcelRepository = parcelRepository;
    }

    /**
     * Считает какие посылки погружены в каждый грузовик и сколько посылок каждого типа
     *
     * @param trucks Список с грузовиками, в которых нужно посчитать посылки
     * @return Список с объектами типа TruckParcelsCounter в каждом объекте содержатся данные
     * о количестве посылок каждого типа, содержащегося в грузовике
     */
    public TruckParcelsCounterWrapper countEachTypeParcelsFromTruckList(List<Truck> trucks) {
        TruckParcelsCounter truckParcelsCounter;

        TruckParcelsCounterWrapper truckParcelsCounterWrapper = new TruckParcelsCounterWrapper();
        for (Truck truck : trucks) {
            Map<Character, Integer> characterIntegerMap = countEachSymbols(truck);

            Map<Parcel, Integer> parcelsAndCount = findParcels(characterIntegerMap);

            truckParcelsCounter = new TruckParcelsCounter();
            truckParcelsCounter.addParcelAndCount(parcelsAndCount);
            truckParcelsCounterWrapper.addTruckParcelsCounter(truckParcelsCounter);
        }

        return truckParcelsCounterWrapper;
    }

    private Map<Parcel, Integer> findParcels(Map<Character, Integer> charsCounter) {
        List<Parcel> allParcels = parcelRepository.getAllParcels();

        Map<Parcel, Integer> parcelsInTruck = new HashMap<>();
        for (Map.Entry<Character, Integer> charAndHisCounter : charsCounter.entrySet()) {
            Character characterFromTruck = charAndHisCounter.getKey();
            Integer count = charAndHisCounter.getValue();

            Parcel parcel = findParcelBySymbol(allParcels, characterFromTruck);
            if (parcel == null) {
                throw new UnknownPackageException("Посылка с символом: " + characterFromTruck + " не существует");
            } else {
                int quantityOfParcelsInTruck = count / parcel.getArea();
                parcelsInTruck.put(parcel, quantityOfParcelsInTruck);
            }
        }
        return parcelsInTruck;
    }

    private Parcel findParcelBySymbol(List<Parcel> allParcels, char symbol) {
        for (Parcel parcel : allParcels) {
            if (parcel.getSymbol().charAt(0) == symbol) {
                return parcel;
            }
        }
        return null;
    }

    private Map<Character, Integer> countEachSymbols(Truck truck) {
        char[][] truckBody = truck.getTruckBody();

        Map<Character, Integer> charsCounter = new HashMap<>();
        for (int i = 0; i < truckBody.length; i++)
            for (int j = 0; j < truckBody[i].length; j++) {
                if (truckBody[i][j] == ' ') {
                    continue;
                }
                if (!charsCounter.containsKey(truckBody[i][j])) {
                    charsCounter.put(truckBody[i][j], 1);
                    continue;
                }
                if (charsCounter.containsKey(truckBody[i][j])) {
                    Integer count = charsCounter.get(truckBody[i][j]);
                    count++;
                    charsCounter.put(truckBody[i][j], count);
                }
            }

        return charsCounter;
    }
}
