package ru.discomfortdeliverer.controller.shellcontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.model.truck.Truck;
import ru.discomfortdeliverer.model.truck.TruckParcelsCounterWrapper;
import ru.discomfortdeliverer.service.parcel.FileParcelLoadService;
import ru.discomfortdeliverer.service.parcel.ParcelService;
import ru.discomfortdeliverer.service.truck.FileTruckLoadService;
import ru.discomfortdeliverer.service.truck.OptimalTruckLoader;
import ru.discomfortdeliverer.service.truck.ParcelCounterService;
import ru.discomfortdeliverer.service.truck.SimpleTruckLoader;
import ru.discomfortdeliverer.service.truck.UniformTruckLoader;

import java.util.List;

@RequiredArgsConstructor
@ShellComponent
@Slf4j
public class TruckShellController {

    private final FileTruckLoadService fileTruckLoadService;
    private final FileParcelLoadService fileParcelLoadService;
    private final ParcelService parcelService;
    private final ParcelCounterService parcelCounterService;
    private final SimpleTruckLoader simpleTruckLoader;
    private final OptimalTruckLoader optimalTruckLoader;
    private final UniformTruckLoader uniformTruckLoader;


    /**
     * Метод загружает заполненные грузовики из файла json и выводит в консоль список этих грузовиков
     *
     * @param filepath Путь к json-файлу
     * @return Список грузовиков из json-файла
     */
    @ShellMethod(key = "load-trucks-from-json-file", value = "Загрузить грузовики из файла json")
    public List<Truck> loadTrucksFromJsonFile(String filepath) {
        log.info("Вызван метод loadTrucksFromJsonFile, filepath={}", filepath);
        return fileTruckLoadService.loadTrucksFromJsonFile(filepath);
    }

    /**
     * Метод читает посылки из файла, загружает их по одной посылке в один грузовик
     *
     * @param filepath Путь к файлу с посылками
     * @return Список грузовиков с загруженными посылками
     */
    @ShellMethod(key = "simple-loading", value = "Погрузить посылки из файла по грузовикам")
    public List<Truck> simpleParcelsLoadFromFileInTrucks(String filepath) {
        log.info("Вызван метод loadParcelsFromFileInTrucks, filepath={}", filepath);
        List<String> parcelNames = fileParcelLoadService.loadParcelNamesFromFileWithParcelNames(filepath);

        List<Parcel> parcels = parcelService.findParcelsByNames(parcelNames);
        List<Truck> trucks = simpleTruckLoader.loadParcels(parcels);

        log.info("Заполненный список грузовиков trucks={}", trucks);
        return trucks;
    }

    /**
     * Метод читает посылки из файла и загружает их в грузовики и возвращает в консоль список этих грузовиков
     *
     * @param filepath Путь к файлу с посылками
     * @return Список грузовиков с загруженными посылками
     */
    @ShellMethod(key = "optimal-loading", value = "Погрузить посылки из файла по грузовикам")
    public List<Truck> optimalParcelsLoadFromFileInTrucks(String filepath, String truckSize, String maxTruckCount) {
        log.info("Вызван метод loadParcelsFromFileInTrucks, filepath={}", filepath);
        List<String> parcelNames = fileParcelLoadService.loadParcelNamesFromFileWithParcelNames(filepath);

        List<Parcel> parcels = parcelService.findParcelsByNames(parcelNames);
        List<Truck> trucks = optimalTruckLoader.loadParcels(parcels, truckSize, maxTruckCount);

        log.info("Заполненный список грузовиков trucks={}", trucks);
        return trucks;
    }

    @ShellMethod(key = "uniform-loading", value = "Равномерная погрузка посылок")
    public List<Truck> uniformParcelsToTrucksFromParcelNames(String filepath,
                                                             @ShellOption(value = {"--trucksSize"}) String trucksSize,
                                                             @ShellOption(value = {"--trucksCount"}) String trucksCount) {

        log.info("Вызван метод uniformParcelsToTrucksFromParcelNames, filepath={}", filepath);
        List<String> parcelNames = fileParcelLoadService.loadParcelNamesFromFileWithParcelNames(filepath);

        List<Parcel> parcels = parcelService.findParcelsByNames(parcelNames);
        List<Truck> trucks = uniformTruckLoader.loadParcels(parcels, trucksSize, trucksCount);

        log.info("Заполненный список грузовиков trucks={}", trucks);
        return trucks;
    }

    /**
     * Метод загружает посылки в грузовики определенных размеров по именам посылки.
     *
     * @param parcelNames Имена посылок, которые надо загрузать, например: Штанга,Велосипед,Байдерка
     * @param trucksSize  Размеры грузовиков, например: 6x6,6x3,5x5
     * @return Список грузовиков с загруженными в них посылками
     */
    @ShellMethod(key = "load-parcels-to-trucks-from-parcel-names", value = "Погрузить посылки в грузовики, указать " +
            "имена посылок. Имена посылок указывать так: Штанга,Велосипед,Байдерка. Размеры грузовиков указывать так: 6x6,6x3,5x5.")
    public List<Truck> uniformParcelsLoadFromFileInTrucks(String parcelNames,
                                                          @ShellOption(value = {"--trucksSize"}) String trucksSize,
                                                          @ShellOption(value = {"--trucksCount"}) String trucksCount) {

        log.info("Вызван метод loadParcelsToTrucksFromParcelNames, parcelNames={}, trucksSize={}", parcelNames, trucksSize);
        String[] split = parcelNames.split(",");
        List<String> names = List.of(split);

        List<Parcel> parcels = parcelService.findParcelsByNames(names);

        return optimalTruckLoader.loadParcels(parcels, trucksSize, trucksCount);
    }

    /**
     * Метод загружает грузовик в формате json и показывает сколько каких посылок в нем лежит
     *
     * @param pathToJsonTrucks Путь к json файлу с грузовиками
     * @return Отображение посылок и их количества по грузовикам
     */
    @ShellMethod(key = "count-parcels-from-trucks", value = "Показать сколько и каких посылок находятся в грузовиках")
    public TruckParcelsCounterWrapper countParcelsFromTrucks(String pathToJsonTrucks) {
        log.debug("Вызван метод readParcelsFromTrucks, pathToJsonTrucks={}", pathToJsonTrucks);
        List<Truck> trucks = fileTruckLoadService.loadTrucksFromJsonFile(pathToJsonTrucks);

        return parcelCounterService.countEachTypeParcels(trucks);
    }
}
