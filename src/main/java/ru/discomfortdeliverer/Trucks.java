package ru.discomfortdeliverer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Trucks {
    public static List<Package> readPackagesFromFile(String filename) {
        List<Package> packages = new ArrayList<>();
        Package currentPackage = new Package();

        try (Scanner scanner = new Scanner(new File(filename))) {

            while (scanner.hasNextLine()) {

                String line = scanner.nextLine();
                if (line.isEmpty()) {
                    packages.add(currentPackage);
                    currentPackage = new Package();
                }
                else {
                    currentPackage.setArea(Character.getNumericValue(line.charAt(0)));
                    currentPackage.setCharacter(line.charAt(0));
                    currentPackage.addLineLength(line.length());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Файл не найден: " + e.getMessage());
        }
        packages.add(currentPackage);
        return packages;
    }
    public static List<Truck> addPackages(List<Package> packages) {
        List<Truck> loadedTrucks = new ArrayList<>();
        // Сортируем посылки по убыванию площади
        Collections.sort(packages, new Comparator<Package>() {
            @Override
            public int compare(Package p1, Package p2) {
                return Integer.compare(p2.getArea(), p1.getArea());
            }
        });

        Truck truck = new Truck();
        // Добавляем по очереди каждую посылку если есть место
        for (int i = 0; i < packages.size(); i++) {
            Package pack = packages.get(i);
            try {
                int[] startInsertCoordinates = truck.checkForFreeSpace(pack);
                truck.insertPackageByCoordinates(pack, startInsertCoordinates);
            } catch (NotEnoughFreeSpaceException e) {
                System.out.println("Грузовик заполнен");
                loadedTrucks.add(truck);
                truck = new Truck();
                i--;
            }
        }
        loadedTrucks.add(truck);
        return loadedTrucks;
    }
}
