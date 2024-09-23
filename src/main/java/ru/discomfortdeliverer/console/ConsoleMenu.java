package ru.discomfortdeliverer.console;

import lombok.extern.slf4j.Slf4j;
import ru.discomfortdeliverer.exception.InvalidFilePathException;
import ru.discomfortdeliverer.exception.InvalidInputException;
import ru.discomfortdeliverer.exception.UnableToLoadException;
import ru.discomfortdeliverer.parcel.FileParcelLoader;
import ru.discomfortdeliverer.parcel.Parcel;
import ru.discomfortdeliverer.truck.FileTruckLoader;
import ru.discomfortdeliverer.truck.Truck;
import ru.discomfortdeliverer.truck.TruckLoadManager;
import ru.discomfortdeliverer.truck.TruckUtils;
import ru.discomfortdeliverer.view.ConsoleTruckView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Slf4j
public class ConsoleMenu {
    private final Scanner scanner;
    private final FileParcelLoader fileParcelLoader;
    private final TruckLoadManager truckLoadManager;
    private final FileTruckLoader fileTruckLoader;
    private final TruckUtils truckUtils;

    public ConsoleMenu(Scanner scanner, FileParcelLoader fileParcelLoader, TruckLoadManager truckLoadManager, FileTruckLoader fileTruckLoader, TruckUtils truckUtils) {
        this.scanner = scanner;
        this.fileParcelLoader = fileParcelLoader;
        this.truckLoadManager = truckLoadManager;
        this.fileTruckLoader = fileTruckLoader;
        this.truckUtils = truckUtils;
    }

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
                    readParcelsAndTryToLoadInTrucks();
                } catch (UnableToLoadException e) {
                    log.error("Невозможно поместить указанное количество посылок по указанному количеству грузовиков");
                    throw e;
                }
            }
            default -> System.out.println("Неверный ввод");
        }
    }

    private void readParcelsAndTryToLoadInTrucks() throws UnableToLoadException {
        List<Parcel> parcels = new ArrayList<>();
        try {
            System.out.println("Введите путь к файлу: ");
            String filePath = scanner.nextLine();
            log.info("Введен путь к файлу - {}", filePath);

            parcels = fileParcelLoader.loadParcelsFromFile(filePath);

        } catch (InvalidInputException e) {
            log.error("В файле невалидные данные");
        } catch (InvalidFilePathException e) {
            log.error("Указан неверный путь к файлу");
        }

        log.info("Из файла прочитано " + parcels.size() + "посылок");
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
                yield truckLoadManager.evenLoad(parcels, trucksCount);
            }
            case "2" -> {
                log.info("Выбран - эффективный алгоритм погрузки по грузовикам");
                yield truckLoadManager.maxQualityLoad(parcels, trucksCount);
            }
            default -> throw new IllegalStateException("Unexpected value: " + choice);
        };

        ConsoleTruckView.printListOfTrucks(trucks);
        log.info("Метод - readParcelsAndTryToLoadInTrucks(), успешно завершил свою работу");
    }

    public void readParcelsFromFileAndPrintTrucks() {
        List<Parcel> parcels = null;
        try {
            System.out.println("Введите путь к файлу: ");
            String filePath = scanner.nextLine();
            log.info("Введен путь к файлу - {}", filePath);

            parcels = fileParcelLoader.loadParcelsFromFile(filePath);

            List<Truck> trucks = chooseLoadAlgorithm(parcels);

            ConsoleTruckView.printListOfTrucks(trucks);
        } catch (InvalidInputException e) {
            log.error("В файле невалидные данные");
        } catch (InvalidFilePathException e) {
            log.error("Указан неверный путь к файлу");
        }
        log.info("Метод readParcelsFromFileAndPrintTrucks() завершился успешно");
    }

    public List<Truck> chooseLoadAlgorithm(List<Parcel> parcels) {
        System.out.println("Выберите алгоритм: \n\t1. Эффективный\n\t2. Простой");
        String choice = scanner.nextLine();
        log.info("Выбран вариант сортировки - {}", choice);

        return switch (choice) {
            case "1" -> truckLoadManager.optimalLoading(parcels);
            case "2" -> truckLoadManager.oneParcelOneTruckLoad(parcels);
            default -> throw new IllegalStateException("Unexpected value: " + choice);
        };
    }

    public void readTruckFromJsonAndPrintResult() {
        System.out.println("Введите путь к json файлу: ");
        String filePath = scanner.nextLine();
        log.info("Введен путь к файлу - {}", filePath);

        Truck truck = fileTruckLoader.loadTruckFromJsonFile(filePath);
        ConsoleTruckView.printTruckBody(truck);
        Map<String, Integer> stringIntegerMap = truckUtils.countEachTypeParcels(truck);

        System.out.println("Количество посылок в грузовике:");
        System.out.println(stringIntegerMap);
        log.info("Метод readTruckFromJsonAndPrintResult() завершился успешно");
    }
}
