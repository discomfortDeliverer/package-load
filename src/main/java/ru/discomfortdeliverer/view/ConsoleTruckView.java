package ru.discomfortdeliverer.view;

import ru.discomfortdeliverer.truck.Truck;

import java.util.List;

public class ConsoleTruckView {
    public static void printTruckBody(Truck truck) {
        char[][] truckBody = truck.getTruckBody();
        for (int i = truckBody.length - 1; i >= 0; i--) {
            System.out.print("+");
            for (int j = 0; j < truckBody[i].length; j++) {
                System.out.print(truckBody[i][j]);
            }
            System.out.println("+");
        }
        System.out.println("++++++++");
    }

    public static void printListOfTrucks(List<Truck> trucks) {
        for (int i = 0; i < trucks.size(); i++) {
            System.out.println("Грузовик #" + (i + 1));
            printTruckBody(trucks.get(i));
        }
    }
}
