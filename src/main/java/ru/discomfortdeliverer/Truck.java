package ru.discomfortdeliverer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Truck {
    private char[][] truckBody;

    public Truck() {
        this.truckBody = new char[6][6];
    }


    public void insertPackageByCoordinates(Package pack, int[] startInsertCoordinates) {
        int row = startInsertCoordinates[0];
        int col = startInsertCoordinates[1];
        List<Integer> lineLengths = pack.getLineLengths();
        char character = pack.getCharacter();

        for (int i = lineLengths.size() - 1; i >= 0; i--) {
            int lineLength = lineLengths.get(i);
            for (int j = 0; j < lineLength; j++) {
                if (col + j >= truckBody[0].length) {
                    // Обработка выхода за границы
                    break;
                }
                truckBody[row][col + j] = character;
            }
            row--;
        }
    }

    public int[] checkForFreeSpace(Package pack) throws NotEnoughFreeSpaceException {
        List<Integer> lineLengths = pack.getLineLengths();
        int row = 5;
        boolean found = false;

        for (int i = lineLengths.size() - 1; i >= 0; i--) {
            int lineLength = lineLengths.get(i);
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
                        // если мы на верхней строчке и еще остались не проверенные ряды, то кидаем исключение
                        if (row == 0 && i != 0) throw new NotEnoughFreeSpaceException();
                        found = true;
                        return new int[] {row, col};
                    }
                }
            }
            if (found) break;
            row = 5; // Сброс row для следующей итерации
        }
        throw new NotEnoughFreeSpaceException();
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
}
