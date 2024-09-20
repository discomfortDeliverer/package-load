package ru.discomfortdeliverer.console;

import lombok.extern.slf4j.Slf4j;
import ru.discomfortdeliverer.exception.InvalidFilePathException;
import ru.discomfortdeliverer.exception.InvalidInputException;
import ru.discomfortdeliverer.exception.UnableToLoadException;
import ru.discomfortdeliverer.parcel.FileParcelLoader;
import ru.discomfortdeliverer.parcel.Parcel;
import ru.discomfortdeliverer.parcel.ParcelInputValidator;
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
    private Scanner scanner = new Scanner(System.in);
    private FileParcelLoader fileParcelLoader = new FileParcelLoader(new ParcelInputValidator());
    private TruckLoadManager truckLoadManager = new TruckLoadManager();
    private FileTruckLoader fileTruckLoader = new FileTruckLoader();
    private TruckUtils truckUtils = new TruckUtils();

    public void startConsoleMenu() {
        System.out.println("Выберите режим:");
        System.out.println("\t1. Загрузка посылок из файла.");
        System.out.println("\t2. Загрузка грузовиков из файла json.");
        System.out.println("\t3. Загрузка посылок из файла и погрузка их в определенное количество грузовиков.");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                readParcelMode();
                break;
            case "2":
                readTruckMode();
                break;
            case "3":
                try {
                    readParcelsAndTryToLoadInTrucksMode();
                } catch (UnableToLoadException e) {
                    System.out.println("Невозможно поместить");
                }
                break;
            default: System.out.println("Неверный ввод");
        }
    }

    private void readParcelsAndTryToLoadInTrucksMode() throws UnableToLoadException {
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

        System.out.println("Обнаружено " + parcels.size() + "посылок");
        System.out.println("Введите количество грузовиков, по которым погрузить: ");
        int trucksCount = Integer.parseInt(scanner.nextLine());

        System.out.println("Выберите алгоритм погрузки: ");
        System.out.println("\n1.Равномерная погрузка по грузовикам.");
        System.out.println("\n2.Эффективная погрузка по грузовикам.");

        String choice = scanner.nextLine();
        List<Truck> trucks = new ArrayList<>();
        switch (choice) {
            case "1":
                trucks = truckLoadManager.evenLoad(parcels, trucksCount);

                break;
            case "2":
                trucks = truckLoadManager.maxQualityLoad(parcels, trucksCount);
                break;
        }

        ConsoleTruckView.printListOfTrucks(trucks);
    }

    public void readParcelMode() {
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
    }

    public List<Truck> chooseLoadAlgorithm(List<Parcel> parcels) {
        System.out.println("Выберите алгоритм: \n\t1. Эффективный\n\t2. Простой");
        String choice = scanner.nextLine();
        log.info("Выбран вариант сортировки - {}", choice);

        List<Truck> trucks = new ArrayList<>();
        switch (choice) {
            case "1":
                trucks = truckLoadManager.optimalLoading(parcels);
                break;
            case "2":
                trucks = truckLoadManager.oneParcelOneTruckLoad(parcels);
                break;
        }
        return trucks;
    }

    public void readTruckMode() {
        System.out.println("Введите путь к json файлу: ");
        String filePath = scanner.nextLine();

        Truck truck = fileTruckLoader.loadTruckFromJsonFile(filePath);
        ConsoleTruckView.printTruckBody(truck);
        Map<String, Integer> stringIntegerMap = truckUtils.countEachTypeParcels(truck);

        System.out.println("Количество посылок в грузовике:");
        System.out.println(stringIntegerMap);
    }
}
