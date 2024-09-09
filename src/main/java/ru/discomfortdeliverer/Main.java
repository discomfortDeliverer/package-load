package ru.discomfortdeliverer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Введите путь к файлу: ");
        String filePath = scanner.nextLine();
        System.out.println("Выберите алгоритм: \n\t1. Эффективный\n\t2. Простой");
        String choice = scanner.nextLine();
        
        List<Package> packages = Trucks.readPackagesFromFile(filePath);
        List<Truck> trucks = new ArrayList<>();
        switch (choice) {
            case "1":
                trucks = Trucks.addPackagesEffectively(packages);
                break;
            case "2":
                trucks = Trucks.addPackagesSimply(packages);
                break;
        }

        for (int i=0; i<trucks.size(); i++) {
            System.out.println(i + 1 + "-й грузовик");
            trucks.get(i).print();
        }
    }
}
