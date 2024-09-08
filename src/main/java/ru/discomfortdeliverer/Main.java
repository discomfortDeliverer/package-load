package ru.discomfortdeliverer;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Truck truck = new Truck();
        List<Package> packages = Trucks.readPackagesFromFile("src/main/resources/pack.txt");
        List<Truck> trucks = Trucks.addPackages(packages);

        for (int i=0; i<trucks.size(); i++) {
            System.out.println(i + 1 + "-й грузовик");
            trucks.get(i).print();
        }
    }
}
