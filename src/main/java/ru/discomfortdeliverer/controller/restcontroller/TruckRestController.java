package ru.discomfortdeliverer.controller.restcontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.model.truck.Truck;
import ru.discomfortdeliverer.model.truck.TruckParcelsCounterWrapper;
import ru.discomfortdeliverer.service.parcel.FileParcelLoadService;
import ru.discomfortdeliverer.service.parcel.ParcelService;
import ru.discomfortdeliverer.service.truck.OptimalTruckLoader;
import ru.discomfortdeliverer.service.truck.ParcelCounterService;
import ru.discomfortdeliverer.service.truck.SimpleTruckLoader;
import ru.discomfortdeliverer.service.truck.UniformTruckLoader;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TruckRestController {

    private final ParcelCounterService parcelCounterService;
    private final FileParcelLoadService fileParcelLoadService;
    private final ParcelService parcelService;
    private final SimpleTruckLoader simpleTruckLoader;
    private final OptimalTruckLoader optimalTruckLoader;
    private final UniformTruckLoader uniformTruckLoader;

    @PostMapping("/trucks/count-parcels")
    public TruckParcelsCounterWrapper countParcelsFromTrucks(@RequestBody List<Truck> trucks) {
        log.info("Входной парметр trucks={}", trucks);
        TruckParcelsCounterWrapper truckParcelsCounterWrapper = parcelCounterService.countEachTypeParcels(trucks);
        log.info("Результат подсчета посылок - {}", truckParcelsCounterWrapper);
        return truckParcelsCounterWrapper;
    }

    @PostMapping("/trucks/simple-load")
    public List<Truck> loadBySimpleRule(@RequestBody String parcelNames) {
        log.info("Входной параметр parcelNames={}", parcelNames);
        List<String> parcelNamesList = fileParcelLoadService.splitLineWithParcelNames(parcelNames);

        List<Parcel> parcels = parcelService.findParcelsByNames(parcelNamesList);
        List<Truck> trucks = simpleTruckLoader.loadParcels(parcels);

        log.info("Заполненный список грузовиков trucks={}", trucks);
        return trucks;
    }

    @PostMapping("/trucks/optimal-load")
    public List<Truck> loadByOptimalRule(@RequestBody String parcelNames,
                                         @RequestParam("truck-size") String truckSize,
                                         @RequestParam("max-truck-count") String maxTruckCount) {
        log.info("Входные параметры parcelNames={}, truckSize={}, maxTruckCount={}",
                parcelNames,
                truckSize,
                maxTruckCount);
        List<String> parcelNamesList = fileParcelLoadService.splitLineWithParcelNames(parcelNames);
        ;

        List<Parcel> parcels = parcelService.findParcelsByNames(parcelNamesList);
        List<Truck> trucks = optimalTruckLoader.loadParcels(parcels, truckSize, maxTruckCount);

        log.info("Заполненный список грузовиков trucks={}", trucks);
        return trucks;
    }

    @PostMapping("/trucks/uniform-load")
    public List<Truck> loadByUniformRule(@RequestBody String parcelNames,
                                         @RequestParam("truck-size") String truckSize,
                                         @RequestParam("max-truck-count") String maxTruckCount) {

        log.info("Входные параметры, parcelNames={}, truckSize={}, maxTruckCount={}",
                parcelNames,
                truckSize,
                maxTruckCount);
        List<String> parcelNamesList = fileParcelLoadService.splitLineWithParcelNames(parcelNames);

        List<Parcel> parcels = parcelService.findParcelsByNames(parcelNamesList);
        List<Truck> trucks = uniformTruckLoader.loadParcels(parcels, truckSize, maxTruckCount);

        log.info("Заполненный список грузовиков trucks={}", trucks);
        return trucks;
    }
}
