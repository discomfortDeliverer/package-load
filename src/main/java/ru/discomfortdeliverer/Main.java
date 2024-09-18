package ru.discomfortdeliverer;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.discomfortdeliverer.exception.InvalidFilePathException;
import ru.discomfortdeliverer.exception.InvalidInputException;
import ru.discomfortdeliverer.parcel.FileParcelLoader;
import ru.discomfortdeliverer.parcel.Parcel;
import ru.discomfortdeliverer.parcel.ParcelInputValidator;
import ru.discomfortdeliverer.truck.Truck;
import ru.discomfortdeliverer.truck.TruckLoadManager;
import ru.discomfortdeliverer.view.ConsoleTruckView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FileParcelLoader fileParcelLoader = new FileParcelLoader(new ParcelInputValidator());
        TruckLoadManager truckLoadManager = new TruckLoadManager();

        List<Parcel> parcels = null;
        try {
            System.out.println("Введите путь к файлу: ");
            String filePath = scanner.nextLine();
            log.info("Введен путь к файлу - {}", filePath);
            parcels = fileParcelLoader.loadParcelsFromFile(filePath);

            System.out.println("Выберите алгоритм: \n\t1. Эффективный\n\t2. Простой");
            String choice = scanner.nextLine();
            List<Truck> trucks = new ArrayList<>();
            switch (choice) {
                case "1":
                    trucks = truckLoadManager.optimalLoading(parcels);
                    break;
                case "2":
                    trucks = truckLoadManager.oneParcelOneTruckLoad(parcels);
                    break;
            }

            ConsoleTruckView.printListOfTrucks(trucks);
        } catch (InvalidInputException e) {
            System.out.println("В файле невалидные данные");
            throw new RuntimeException(e);
        } catch (InvalidFilePathException e) {
            System.out.println("Указан неверный путь к файлу");
            throw new RuntimeException(e);
        }

    }
}
