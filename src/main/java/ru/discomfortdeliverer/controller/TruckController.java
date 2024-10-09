package ru.discomfortdeliverer.controller;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

    /**
     * Метод загружает заполненные грузовики из файла json и выводит в консоль список этих грузовиков
     * @param filepath Путь к json-файлу
     * @return Список грузовиков из json-файла
     */
    @ShellMethod(key = "load-trucks-from-json-file", value = "Загрузить грузовики из файла json")
    public List<Truck> loadTrucksFromJsonFile(String filepath) {
        log.info("Вызван метод loadTrucksFromJsonFile, filepath={}", filepath);
        return fileTruckLoadService.loadTrucksFromJsonFile(filepath);
    }

    /**
     * Метод читает посылки из файла и загружает их в грузовики и возвращает в консоль список этих грузовиков
     * @param filepath Путь к файлу с посылками
     * @param effective Эффективная загрузка - загрузить в минимальное количество грузовиков
     * @param simple Простая загрузка - загрузить по 1 посылке в один грузовик
     * @return Список грузовиков с загруженными посылками
     */
    @ShellMethod(key = "load-parcels-from-file", value = "Погрузить посылки из файла по грузовикам, " +
            "--e - эффективная погрузка, --s - простая погрузка")
    public List<Truck> loadParcelsFromFileInTrucks(String filepath,
           @ShellOption(value = {"--e","--effective"}, defaultValue = "false") boolean effective,
           @ShellOption(value = {"--s","--simple"}, defaultValue = "false") boolean simple) {

        log.info("Вызван метод loadParcelsFromFileInTrucks, filepath={}, effective={}, simple={}",
                filepath, effective, simple);
        List<Parcel> parcels = fileParcelLoadService.loadParcelsFromFile(filepath);

        List<Truck> trucks = new ArrayList<>();
        if (effective) {
            trucks = parcelLoadInTruckService.optimalLoading(parcels);
        }
        if (simple){
            trucks = parcelLoadInTruckService.oneParcelOneTruckLoad(parcels);
        }
        log.info("Заполненный список грузовиков trucks={}", trucks);
        return trucks;
    }

    /**
     * Метод загружает посылки из файла и погружает их в грузовики с определенными размерами
     * @param filepath Путь к файлу с посылками
     * @param trucksSize Размеры грузовиков, например: 6x6,6x3,5x5
     * @return Список грузовиков с загруженными в них посылками
     */
    @ShellMethod(key = "load-parcels-to-trucks-from-file", value = "Загрузки посылки из файла и погрузить" +
            " их в определенное количество грузовиков. Размеры грузовиков указывать так: 6x6,6x3,5x5")
    public List<Truck> loadParcelsToTrucksFromFile(String filepath,
           @ShellOption(value = {"--trucksSize"}, defaultValue = "6x6") String trucksSize) {

        log.info("Вызван метод loadParcelsToTrucksFromFile, filepath={}, trucksSize={}", filepath, trucksSize);
        List<Parcel> parcels = fileParcelLoadService.loadParcelsFromFile(filepath);

        return parcelLoadInTruckService.loadParcelsToTrucks(parcels, trucksSize);
    }

    /**
     * Метод загружает посылки в грузовики определенных размеров по именам посылки.
     * @param parcelNames Имена посылок, которые надо загрузать, например: Штанга,Велосипед,Байдерка
     * @param trucksSize Размеры грузовиков, например: 6x6,6x3,5x5
     * @return Список грузовиков с загруженными в них посылками
     */
    @ShellMethod(key = "load-parcels-to-trucks-from-parcel-names", value = "Погрузить посылки в грузовики, указать " +
            "имена посылок. Имена посылок указывать так: Штанга,Велосипед,Байдерка. Размеры грузовиков указывать так: 6x6,6x3,5x5.")
    public List<Truck> loadParcelsToTrucksFromParcelNames(String parcelNames,
                                                   @ShellOption(value = {"--trucksSize"}, defaultValue = "6x6") String trucksSize) {

        log.info("Вызван метод loadParcelsToTrucksFromParcelNames, parcelNames={}, trucksSize={}", parcelNames, trucksSize);
        List<Parcel> parcels = parcelService.findParcelsByNames(parcelNames);

        return parcelLoadInTruckService.loadParcelsToTrucks(parcels, trucksSize);
    }

    /**
     * Метод загружает грузовик в формате json и показывает сколько каких посылок в нем лежит
     * @param pathToJsonTrucks Путь к json файлу с грузовиками
     * @return Отображение посылок и их количества по грузовикам
     */
    @ShellMethod(key = "read-parcels-from-trucks", value = "Показать сколько и каких посылок находятся в грузовиках")
    public TruckParcelsCounterWrapper readParcelsFromTrucks(String pathToJsonTrucks) {
        log.debug("Вызван метод readParcelsFromTrucks, pathToJsonTrucks={}", pathToJsonTrucks);
        List<Truck> trucks = fileTruckLoadService.loadTrucksFromJsonFile(pathToJsonTrucks);

        return parcelCounterService.countEachTypeParcelsFromTruckList(trucks);
    }
}
