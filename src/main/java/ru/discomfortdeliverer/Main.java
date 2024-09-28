package ru.discomfortdeliverer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.discomfortdeliverer.console.ConsoleMenu;
import ru.discomfortdeliverer.parcel.FileParcelLoader;
import ru.discomfortdeliverer.parcel.ParcelInputValidator;
import ru.discomfortdeliverer.truck.FileTruckLoader;
import ru.discomfortdeliverer.truck.TruckLoadManager;
import ru.discomfortdeliverer.truck.TruckUtils;
import java.util.Scanner;

@Slf4j
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
//        Scanner scanner = new Scanner(System.in);
//        FileParcelLoader fileParcelLoader = new FileParcelLoader(new ParcelInputValidator());
//        TruckLoadManager truckLoadManager = new TruckLoadManager();
//        FileTruckLoader fileTruckLoader = new FileTruckLoader();
//        TruckUtils truckUtils = new TruckUtils();
//        ConsoleMenu consoleMenu = new ConsoleMenu(scanner, fileParcelLoader, truckLoadManager
//        ,fileTruckLoader, truckUtils);
//        consoleMenu.startConsoleMenu();
    }
}
