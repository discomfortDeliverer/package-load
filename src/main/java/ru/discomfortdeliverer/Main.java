package ru.discomfortdeliverer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
//        Scanner scanner = new Scanner(System.in);
//        FileParcelLoadService fileParcelLoader = new FileParcelLoadService(new ParcelInputValidator());
//        ParcelLoadInTruckService truckLoadManager = new ParcelLoadInTruckService();
//        FileTruckLoadService fileTruckLoader = new FileTruckLoadService();
//        TruckUtils truckUtils = new TruckUtils();
//        ConsoleMenu consoleMenu = new ConsoleMenu(scanner, fileParcelLoader, truckLoadManager
//        ,fileTruckLoader, truckUtils);
//        consoleMenu.startConsoleMenu();
    }
}
