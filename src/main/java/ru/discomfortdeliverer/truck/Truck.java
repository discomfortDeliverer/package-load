package ru.discomfortdeliverer.truck;

import lombok.extern.slf4j.Slf4j;
import ru.discomfortdeliverer.Coordinates;
import ru.discomfortdeliverer.parcel.Parcel;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class Truck {
    private static final int TRUCK_HEIGHT = 6;
    private static final int TRUCK_LENGTH = 6;
    private char[][] truckBody;

    public Truck() {
        this.truckBody = new char[TRUCK_HEIGHT][TRUCK_LENGTH];

        for (int i = 0; i < truckBody.length; i++)
            for (int j = 0; j < truckBody[i].length; j++) {
                truckBody[i][j] = ' ';
            }
        log.debug("Создан объект Truck");
    }

    public char[][] getTruckBody() {
        return truckBody;
    }

    public boolean canFitParcelAtCoordinates(Parcel parcel, int row, int col) {
        log.debug("Проверка, можно ли поместить посылку {} по координатам row={}, col={}", parcel, row, col);
        if (parcel.getLength() + col > TRUCK_LENGTH || parcel.getHeight() + row > TRUCK_HEIGHT)
            return false;

        for (int i = 0; i < parcel.getHeight(); i++)
            for (int j = 0; j < parcel.getLength(); j++) {
                if(parcel.getBody()[i][j] != ' ' && truckBody[row + i][col + j] != ' ')
                    return false;
            }

        // Проверка опоры
        return hasBottomSupport(parcel, row, col);
    }

    private boolean hasBottomSupport(Parcel parcel, int row, int col) {
        log.debug("Проверка, есть ли опора под посылкой {}", parcel);
        if (row == 0) return true;
        
        row--;
        int halfParcelLength = parcel.getLength() / 2;
        int halfBottomRowCoordinate = halfParcelLength + col;
        for (; col <= halfBottomRowCoordinate; col++) {
            if (truckBody[row][col] == ' ') return false;
        }

        return true;
    }

    public void placeParcelByCoordinates(Parcel parcel, int row, int col) {
        log.debug("Помещаем посылку {}, в грузовик {} по координатам row={}, col={}", parcel, this, row, col);
        char[][] parcelBody = parcel.getBody();

        for (int i = 0; i < parcel.getHeight(); i++)
            for (int j = 0; j < parcel.getLength(); j++)
                truckBody[row + i][col + j] = parcelBody[i][j];
    }

    public Optional<Coordinates> findCoordinatesToPlace(Parcel parcel) {
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 6; j++) {
                if (this.canFitParcelAtCoordinates(parcel, i, j)) {
                    Coordinates coordinates = new Coordinates();
                    coordinates.setRow(i);
                    coordinates.setCol(j);
                    return Optional.of(coordinates);
                }
            }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "Truck{" +
                "truckBody=" + Arrays.deepToString(truckBody) +
                '}';
    }
}
