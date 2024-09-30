package ru.discomfortdeliverer.view;

import ru.discomfortdeliverer.model.truck.Truck;
import ru.discomfortdeliverer.truck.TruckParcelsCounter;

import java.util.List;

public class ConsoleTruckView {
    /**
     * Выводит на консоль содержимое грузовика
     * @param truck Грузовик, содержимое которого мы хотим увидеть в консоли
     */
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

    /**
     * Выводит на консоль содержимое каждого грузовика из списка
     * @param trucks Список с грузовиками, содержимое которых мы хотим посмотреть
     */
    public static void printListOfTrucks(List<Truck> trucks) {
        for (int i = 0; i < trucks.size(); i++) {
            System.out.println("Грузовик #" + (i + 1));
            printTruckBody(trucks.get(i));
        }
    }

    /**
     * Выводит на консоль информацию о том, какие посылки и в каком количестве находятся в списке
     * грузовиков
     * @param truckParcelsCounters Список с объектами TruckParcelsCounter с информацией по каждому
     * грузовику
     */
    public static void printListOfTruckParcelCounter(List<TruckParcelsCounter> truckParcelsCounters) {
        int i = 1;
        for (TruckParcelsCounter TruckParcelsCounter : truckParcelsCounters) {
            System.out.println("Грузовик #" + i);
            System.out.println(TruckParcelsCounter);
            i++;
        }

    }
}
