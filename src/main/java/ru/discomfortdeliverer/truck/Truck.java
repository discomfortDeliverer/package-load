package ru.discomfortdeliverer.truck;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.extern.slf4j.Slf4j;
import ru.discomfortdeliverer.parcel.Coordinates;
import ru.discomfortdeliverer.model.Parcel;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class Truck {
    @JsonIgnore
    private static final int TRUCK_HEIGHT = 6;
    @JsonIgnore
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
    public void setTruckBody(char[][] truckBody) {
        this.truckBody = truckBody;
    }

    /**
     * Проверяет то, возможно ли поместить посылку в грузовик по указанным координатам
     * @param parcel Посылка, которую нужно поместить
     * @param row Координата по вертикали
     * @param col Координата по горизонтали
     * @return Результат, поместиться ли посылка по данным координатам или нет
     */
    public boolean canFitParcelAtCoordinates(Parcel parcel, int row, int col) {
        log.debug("Проверка, можно ли поместить посылку {} по координатам row={}, col={}", parcel, row, col);
        if (parcel.getLength() + col > TRUCK_LENGTH || parcel.getHeight() + row > TRUCK_HEIGHT)
            return false;

        for (int i = 0; i < parcel.getHeight(); i++)
            for (int j = 0; j < parcel.getLength(); j++) {
                if (parcel.getForm()[i][j] != ' ' && truckBody[row + i][col + j] != ' ')
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

    /**
     * Помещает посылку по указанным координатам
     * @param parcel Посылка, которая будет помещена по координатам
     * @param row Координата по вертикали, начиная от которой будет вставлена посылка
     * @param col Координата по горизонтали, начиная от которой будет вставлена посылка
     */
    public void placeParcelByCoordinates(Parcel parcel, int row, int col) {
        log.debug("Помещаем посылку {}, в грузовик {} по координатам row={}, col={}", parcel, this, row, col);
        char[][] parcelBody = parcel.getForm();

        for (int i = 0; i < parcel.getHeight(); i++)
            for (int j = 0; j < parcel.getLength(); j++)
                truckBody[row + i][col + j] = parcelBody[i][j];
    }

    /**
     * Находит координаты, по которым можно поместить посылку
     * @param parcel Посылка, координаты для вставки которой мы ищем
     * @return Если координаты найдены, то возвращаем их в Optional,
     * если нет, то возвращаем пустой Optional
     */
    public Optional<Coordinates> findCoordinatesToPlace(Parcel parcel) {
        for (int i = 0; i < TRUCK_HEIGHT; i++)
            for (int j = 0; j < TRUCK_LENGTH; j++) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Truck truck = (Truck) o;
        return Arrays.deepEquals(truckBody, truck.truckBody);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(truckBody);
    }
}
