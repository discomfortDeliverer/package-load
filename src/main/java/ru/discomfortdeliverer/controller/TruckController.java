package ru.discomfortdeliverer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.discomfortdeliverer.model.Parcel;
import ru.discomfortdeliverer.service.parcel.FileParcelLoadService;
import ru.discomfortdeliverer.service.truck.FileTruckLoadService;
import ru.discomfortdeliverer.service.truck.ParcelLoadInTruckService;
import ru.discomfortdeliverer.truck.Truck;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ShellComponent
public class TruckController {
    private final FileTruckLoadService fileTruckLoadService;
    private final FileParcelLoadService fileParcelLoadService;
    private final ParcelLoadInTruckService parcelLoadInTruckService;

    @Autowired
    public TruckController(FileTruckLoadService fileTruckLoadService,
                           FileParcelLoadService fileParcelLoadService,
                           ParcelLoadInTruckService parcelLoadInTruckService) {
        this.fileTruckLoadService = fileTruckLoadService;
        this.fileParcelLoadService = fileParcelLoadService;
        this.parcelLoadInTruckService = parcelLoadInTruckService;
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
}
