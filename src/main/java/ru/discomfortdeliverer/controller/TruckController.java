package ru.discomfortdeliverer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.model.truck.TruckParcelsCounterWrapper;
import ru.discomfortdeliverer.service.parcel.FileParcelLoadService;
import ru.discomfortdeliverer.service.parcel.ParcelService;
import ru.discomfortdeliverer.service.truck.FileTruckLoadService;
import ru.discomfortdeliverer.service.truck.ParcelCounterService;
import ru.discomfortdeliverer.service.truck.ParcelLoadInTruckService;
import ru.discomfortdeliverer.model.truck.Truck;
import ru.discomfortdeliverer.model.truck.TruckParcelsCounter;

import java.util.ArrayList;
import java.util.List;

@ShellComponent
public class TruckController {
    private final FileTruckLoadService fileTruckLoadService;
    private final FileParcelLoadService fileParcelLoadService;
    private final ParcelLoadInTruckService parcelLoadInTruckService;
    private final ParcelService parcelService;
    private final ParcelCounterService parcelCounterService;

    @Autowired
    public TruckController(FileTruckLoadService fileTruckLoadService,
                           FileParcelLoadService fileParcelLoadService,
                           ParcelLoadInTruckService parcelLoadInTruckService,
                           ParcelService parcelService,
                           ParcelCounterService parcelCounterService) {
        this.fileTruckLoadService = fileTruckLoadService;
        this.fileParcelLoadService = fileParcelLoadService;
        this.parcelLoadInTruckService = parcelLoadInTruckService;
        this.parcelService = parcelService;
        this.parcelCounterService = parcelCounterService;
    }

    @ShellMethod(key = "load-trucks-from-json-file", value = "Загрузить грузовики из файла json")
    public List<Truck> loadTrucksFromJsonFile(String filepath) {
        return fileTruckLoadService.loadTrucksFromJsonFile(filepath);
    }

    @ShellMethod(key = "load-parcels-from-file", value = "Погрузить посылки из файла по грузовикам, " +
            "--e - эффективная погрузка, --s - простая погрузка")
    public List<Truck> loadParcelsFromFileInTrucks(String filepath,
           @ShellOption(value = {"--e","--effective"}, defaultValue = "false") boolean effective,
           @ShellOption(value = {"--s","--simple"}, defaultValue = "false") boolean simple) {
        List<Parcel> parcels = fileParcelLoadService.loadParcelsFromFile(filepath);

        List<Truck> trucks = new ArrayList<>();
        if (effective) {
            trucks = parcelLoadInTruckService.optimalLoading(parcels);
        }
        if (simple){
            trucks = parcelLoadInTruckService.oneParcelOneTruckLoad(parcels);
        }
        return trucks;
    }

    @ShellMethod(key = "load-parcels-to-trucks-from-file", value = "Загрузки посылки из файла и погрузить" +
            " их в определенное количество грузовиков. Размеры грузовиков указывать так: 6x6,6x3,5x5")
    public List<Truck> loadParcelsToTrucksFromFile(String filepath,
           @ShellOption(value = {"--trucksSize"}, defaultValue = "6x6") String trucksSize) {
        List<Parcel> parcels = fileParcelLoadService.loadParcelsFromFile(filepath);

        return parcelLoadInTruckService.loadParcelsToTrucks(parcels, trucksSize);
    }

    @ShellMethod(key = "load-parcels-to-trucks-from-parcel-names", value = "Погрузить посылки в грузовики, указать " +
            "имена посылок. Имена посылок указывать так: Штанга,Велосипед,Байдерка. Размеры грузовиков указывать так: 6x6,6x3,5x5.")
    public List<Truck> loadParcelsToTrucksFromParcelNames(String parcelNames,
                                                   @ShellOption(value = {"--trucksSize"}, defaultValue = "6x6") String trucksSize) {

        List<Parcel> parcels = parcelService.findParcelsByNames(parcelNames);

        return parcelLoadInTruckService.loadParcelsToTrucks(parcels, trucksSize);
    }

    @ShellMethod(key = "read-parcels-from-trucks", value = "Показать сколько и каких посылок находятся в грузовиках")
    public TruckParcelsCounterWrapper readParcelsFromTrucks(String pathToJsonTrucks) {
        List<Truck> trucks = fileTruckLoadService.loadTrucksFromJsonFile(pathToJsonTrucks);

        return parcelCounterService.countEachTypeParcelsFromTruckList(trucks);

    }
}
