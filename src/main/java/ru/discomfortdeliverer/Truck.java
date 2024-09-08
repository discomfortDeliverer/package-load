package ru.discomfortdeliverer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Truck {
    private char[][] truckBody;

    public Truck() {
        this.truckBody = new char[6][6];
    }

    public List<Package> readPackagesFromFile(String filename) {
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

    public void addPackages(List<Package> packages) {
        // Сортируем посылки по убыванию площади
        Collections.sort(packages, new Comparator<Package>() {
            @Override
            public int compare(Package p1, Package p2) {
                return Integer.compare(p2.getArea(), p1.getArea());
            }
        });

        // Добавляем по очереди каждую посылку если есть место
        for (Package pack : packages) {
            try {
                int[] startInsertCoordinates = checkForFreeSpace(pack);
                insertPackageByCoordinates(pack, startInsertCoordinates);
            } catch (NotEnoughFreeSpaceException e) {
                System.out.println("Грузовик заполнен");
                throw new RuntimeException(e);
            }
        }

    }

    public void insertPackageByCoordinates(Package pack, int[] startInsertCoordinates) {
        int row = startInsertCoordinates[0];
        int col = startInsertCoordinates[1];
        List<Integer> linelengths = pack.getLinelengths();
        char character = pack.getCharacter();

        for (int i = linelengths.size() - 1; i >= 0; i--) {
            int lineLength = linelengths.get(i);
            for (int j = 0; j < lineLength; j++) {
                if (col + j >= truckBody[0].length) {
                    // Обработка выхода за границы
                    break;
                }
                truckBody[row][col + j] = character;
            }
            row--;
        }

        System.out.println("Добавление пакета");
        print();
    }

    public void print() {
        for (int row = 0; row < 6; row++) {
            System.out.print("+");
            for (int col = 0; col < 6; col++) {
                if (truckBody[row][col] == '\u0000') {
                    System.out.print(" ");
                    continue;
                }
                System.out.print(truckBody[row][col]);
            }
            System.out.println("+");
        }
        System.out.println("++++++++");
    }


    public int[] checkForFreeSpace(Package pack) throws NotEnoughFreeSpaceException {
        List<Integer> linelengths = pack.getLinelengths();
        int row = 5;
        boolean found = false;

        for (int i = linelengths.size() - 1; i >= 0; i--) {
            int lineLength = linelengths.get(i);
            for (; row >= 0; row--) {
                for (int col = 0; col <= 6 - lineLength; col++) {
                    boolean isFree = true;
                    for (int j = 0; j < lineLength; j++) {
                        if (truckBody[row][col + j] != '\u0000') {
                            isFree = false;
                            break;
                        }
                    }
                    if (isFree) {
                        found = true;
                        return new int[] {row, col};
                    }
                }
            }
            if (found) break;
            row = 5; // Сброс row для следующей итерации
        }
        // Если мы сюда дошли, значит вставить некуда
        throw new NotEnoughFreeSpaceException();
    }

}
