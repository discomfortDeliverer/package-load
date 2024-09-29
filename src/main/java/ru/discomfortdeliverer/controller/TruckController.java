package ru.discomfortdeliverer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.discomfortdeliverer.model.Parcel;
import ru.discomfortdeliverer.service.truck.FileTruckLoadService;
import ru.discomfortdeliverer.truck.Truck;

import java.util.List;

@ShellComponent
public class TruckController {
    private final FileTruckLoadService fileTruckLoadService;

    @Autowired
    public TruckController(FileTruckLoadService fileTruckLoadService) {
        this.fileTruckLoadService = fileTruckLoadService;
    }

    @ShellMethod(key = "load-trucks-from-json-file", value = "Загрузить грузовики из файла json")
    public List<Truck> loadTrucksFromJsonFile(String filepath) {
        return fileTruckLoadService.loadTrucksFromJsonFile(filepath);
    }
}
