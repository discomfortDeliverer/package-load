package ru.discomfortdeliverer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import ru.discomfortdeliverer.console.ConsoleMenu;
import ru.discomfortdeliverer.exception.InvalidFilePathException;
import ru.discomfortdeliverer.exception.InvalidInputException;
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
import java.util.Scanner;

@Slf4j
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FileParcelLoader fileParcelLoader = new FileParcelLoader(new ParcelInputValidator());
        TruckLoadManager truckLoadManager = new TruckLoadManager();
        FileTruckLoader fileTruckLoader = new FileTruckLoader();
        TruckUtils truckUtils = new TruckUtils();
        ConsoleMenu consoleMenu = new ConsoleMenu(scanner, fileParcelLoader, truckLoadManager
        ,fileTruckLoader, truckUtils);
        consoleMenu.startConsoleMenu();
    }
}
