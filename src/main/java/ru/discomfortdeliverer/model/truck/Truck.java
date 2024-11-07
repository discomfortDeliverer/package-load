package ru.discomfortdeliverer.model.truck;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.discomfortdeliverer.model.parcel.Coordinates;
import ru.discomfortdeliverer.model.parcel.Parcel;

import java.util.Arrays;

@Slf4j
@Getter
@Setter
public class Truck {

    @JsonProperty("truckHeight")
    private int truckHeight;
    @JsonProperty("truckLength")
    private int truckLength;
    @JsonProperty("truckBody")
    private char[][] truckBody;

    public Truck(int truckHeight, int truckLength) {
        this.truckHeight = truckHeight;
        this.truckLength = truckLength;
        this.truckBody = new char[truckHeight][truckLength];

        for (int i = 0; i < truckBody.length; i++)
            for (int j = 0; j < truckBody[i].length; j++) {
                truckBody[i][j] = ' ';
            }
    }

    public Truck() {
        this.truckHeight = 6;
        this.truckLength = 6;
        this.truckBody = new char[truckHeight][truckLength];

        for (int i = 0; i < truckBody.length; i++)
            for (int j = 0; j < truckBody[i].length; j++) {
                truckBody[i][j] = ' ';
            }
        log.debug("Создан объект Truck");
    }

    public void reverseTruckBody() {
        int rows = this.truckHeight;
        int cols = this.truckLength;
        char[][] rotated = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotated[i][j] = this.truckBody[rows - 1 - i][j];
            }
        }

        this.truckBody = rotated;
    }

    /**
     * Проверяет то, возможно ли поместить посылку в грузовик по указанным координатам
     *
     * @param parcel Посылка, которую нужно поместить
     * @param row    Координата по вертикали
     * @param col    Координата по горизонтали
     * @return Результат, поместиться ли посылка по данным координатам или нет
     */
    public boolean canFitParcelAtCoordinates(Parcel parcel, int row, int col) {
        log.debug("Проверка, можно ли поместить посылку {} по координатам row={}, col={}", parcel, row, col);
        if (parcel.getLength() + col > truckLength || parcel.getHeight() + row > truckHeight)
            return false;

        for (int i = 0; i < parcel.getHeight(); i++)
            for (int j = 0; j < parcel.getLength(); j++) {
                if (parcel.getForm()[i][j] != ' ' && truckBody[row + i][col + j] != ' ')
                    return false;
            }

        // Проверка опоры
        return hasBottomSupport(parcel, row, col);
    }

    /**
     * Помещает посылку по указанным координатам
     *
     * @param parcel Посылка, которая будет помещена по координатам
     * @param row    Координата по вертикали, начиная от которой будет вставлена посылка
     * @param col    Координата по горизонтали, начиная от которой будет вставлена посылка
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
     *
     * @param parcel Посылка, координаты для вставки которой мы ищем
     * @return Если координаты найдены, то возвращаем их в Optional,
     * если нет, то возвращаем пустой Optional
     */
    public Coordinates findCoordinatesToPlace(Parcel parcel) {
        log.debug("Вызван метод findCoordinatesToPlace, parcel={}", parcel);
        for (int i = 0; i < truckHeight; i++)
            for (int j = 0; j < truckLength; j++) {
                if (this.canFitParcelAtCoordinates(parcel, i, j)) {
                    Coordinates coordinates = new Coordinates();
                    coordinates.setRow(i);
                    coordinates.setCol(j);
                    log.debug("Найдена координата для вставки посылки={}, row={}, col={}", parcel, i, j);
                    return coordinates;
                }
            }
        log.debug("Найдена координата для вставки посылки={} не найдена", parcel);
        return null;
    }

    private boolean hasBottomSupport(Parcel parcel, int row, int col) {
        log.debug("Проверка, есть ли опора под посылкой {}", parcel);
        if (row == 0) {
            return true;
        }

        row--;
        int halfParcelLength = parcel.getLength() / 2;
        int halfBottomRowCoordinate = halfParcelLength + col;
        for (; col <= halfBottomRowCoordinate; col++) {
            if (truckBody[row][col] == ' ') return false;
        }

        return true;
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
