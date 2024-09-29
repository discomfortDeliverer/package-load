package ru.discomfortdeliverer.console;

import lombok.extern.slf4j.Slf4j;
import ru.discomfortdeliverer.exception.InvalidFilePathException;
import ru.discomfortdeliverer.exception.InvalidInputException;
import ru.discomfortdeliverer.exception.UnableToLoadException;
import ru.discomfortdeliverer.service.parcel.FileParcelLoadService;
import ru.discomfortdeliverer.model.Parcel;
import ru.discomfortdeliverer.service.truck.FileTruckLoadService;
import ru.discomfortdeliverer.truck.Truck;
import ru.discomfortdeliverer.service.truck.ParcelLoadInTruckService;
import ru.discomfortdeliverer.truck.TruckParcelsCounter;
import ru.discomfortdeliverer.truck.TruckUtils;
import ru.discomfortdeliverer.view.ConsoleTruckView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class ConsoleMenu {
    private final Scanner scanner;
    private final FileParcelLoadService fileParcelLoadService;
    private final ParcelLoadInTruckService parcelLoadInTruckService;
    private final FileTruckLoadService fileTruckLoadService;
    private final TruckUtils truckUtils;

    public ConsoleMenu(Scanner scanner, FileParcelLoadService fileParcelLoadService, ParcelLoadInTruckService parcelLoadInTruckService, FileTruckLoadService fileTruckLoadService, TruckUtils truckUtils) {
        this.scanner = scanner;
        this.fileParcelLoadService = fileParcelLoadService;
        this.parcelLoadInTruckService = parcelLoadInTruckService;
        this.fileTruckLoadService = fileTruckLoadService;
        this.truckUtils = truckUtils;
    }

    /**
     * Метод запускает консольное меню
     */
    public void startConsoleMenu() {
        System.out.println("Выберите режим:");
        System.out.println("\t1. Загрузка посылок из файла.");
        System.out.println("\t2. Загрузка грузовиков из файла json.");
        System.out.println("\t3. Загрузка посылок из файла и погрузка их в определенное количество грузовиков.");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1" -> {
                log.info("Выбран режим загрузки посылок из файла");
                readParcelsFromFileAndPrintTrucks();
            }
            case "2" -> {
                log.info("Выбран режим загрузки грузовиков из файла json");
                readTruckFromJsonAndPrintResult();
            }
            case "3" -> {
                log.info("Выбран режим загрузки посылок из файла и погрузка их в определенное количество грузовиков");
                try {
                    readParcels();
                } catch (UnableToLoadException e) {
                    log.error("Невозможно поместить указанное количество посылок по указанному количеству грузовиков");
                    throw e;
                }
            }
            default -> System.out.println("Неверный ввод");
        }
    }

    private void readParcels() throws UnableToLoadException {
        List<Parcel> parcels = new ArrayList<>();
        try {
            System.out.println("Введите путь к файлу: ");
            String filePath = scanner.nextLine();
            log.info("Введен путь к файлу - {}", filePath);

            parcels = fileParcelLoadService.loadParcelsFromFile(filePath);

        } catch (InvalidInputException e) {
            log.error("В файле невалидные данные");
        } catch (InvalidFilePathException e) {
            log.error("Указан неверный путь к файлу");
        }

        log.info("Из файла прочитано " + parcels.size() + "посылок");

        readTrucksCountAndChooseLoadAlgorithm(parcels);
    }

    private void readTrucksCountAndChooseLoadAlgorithm(List<Parcel> parcels) {
        System.out.println("Введите количество грузовиков, по которым погрузить: ");
        int trucksCount = Integer.parseInt(scanner.nextLine());
        log.info("Введенное количество грузовиков - {} ", trucksCount);

        System.out.println("Выберите алгоритм погрузки: ");
        System.out.println("\n1.Равномерная погрузка по грузовикам.");
        System.out.println("\n2.Эффективная погрузка по грузовикам.");

        String choice = scanner.nextLine();
        List<Truck> trucks = switch (choice) {
            case "1" -> {
                log.info("Выбран - равномерный алгоритм погрузки по грузовикам");
                yield parcelLoadInTruckService.evenLoad(parcels, trucksCount);
            }
            case "2" -> {
                log.info("Выбран - эффективный алгоритм погрузки по грузовикам");
                yield parcelLoadInTruckService.maxQualityLoad(parcels, trucksCount);
            }
            default -> throw new IllegalStateException("Unexpected value: " + choice);
        };

        ConsoleTruckView.printListOfTrucks(trucks);
        log.info("Метод - readTrucksCountAndChooseLoadAlgorithm(), успешно завершил свою работу");
    }

    private void readParcelsFromFileAndPrintTrucks() {
        try {
            System.out.println("Введите путь к файлу: ");
            String filePath = scanner.nextLine();
            log.info("Введен путь к файлу - {}", filePath);

            List<Parcel> parcels = fileParcelLoadService.loadParcelsFromFile(filePath);

            List<Truck> trucks = chooseLoadAlgorithm(parcels);

            ConsoleTruckView.printListOfTrucks(trucks);
        } catch (InvalidInputException e) {
            log.error("В файле невалидные данные");
            throw e;
        } catch (InvalidFilePathException e) {
            log.error("Указан неверный путь к файлу");
            throw e;
        }
        log.info("Метод readParcelsFromFileAndPrintTrucks() завершился успешно");
    }

    private List<Truck> chooseLoadAlgorithm(List<Parcel> parcels) {
        System.out.println("Выберите алгоритм: \n\t1. Эффективный\n\t2. Простой");
        String choice = scanner.nextLine();
        log.info("Выбран вариант сортировки - {}", choice);

        return switch (choice) {
            case "1" -> parcelLoadInTruckService.optimalLoading(parcels);
            case "2" -> parcelLoadInTruckService.oneParcelOneTruckLoad(parcels);
            default -> throw new IllegalStateException("Unexpected value: " + choice);
        };
    }

    private void readTruckFromJsonAndPrintResult() {
        System.out.println("Выберите режим загрузки из json-файла:");
        System.out.println("\t1. Загрузка одного грузовика.");
        System.out.println("\t2. Загрузка нескольких грузовиков.");
        String choice = scanner.nextLine();

        System.out.println("Введите путь к json файлу: ");
        String filePath = scanner.nextLine();
        log.info("Введен путь к файлу - {}", filePath);

        switch (choice) {
            case "1":
                Truck truck = fileTruckLoadService.loadTruckFromJsonFile(filePath);
                ConsoleTruckView.printTruckBody(truck);
                TruckParcelsCounter stringIntegerMap = truckUtils.countEachTypeParcels(truck);
                System.out.println("Количество посылок в грузовике:");
                System.out.println(stringIntegerMap);
                break;
            case "2":
                List<Truck> trucks = fileTruckLoadService.loadTrucksFromJsonFile(filePath);
                ConsoleTruckView.printListOfTrucks(trucks);
                List<TruckParcelsCounter> truckParcelsCounters = truckUtils.countEachTypeParcelsFromTruckList(trucks);
                System.out.println("Количество посылок в грузовиках:");
                ConsoleTruckView.printListOfTruckParcelCounter(truckParcelsCounters);
                break;
        }

        log.info("Метод readTruckFromJsonAndPrintResult() завершился успешно");
    }
}
