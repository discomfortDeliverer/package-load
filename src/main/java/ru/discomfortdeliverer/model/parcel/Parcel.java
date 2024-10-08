package ru.discomfortdeliverer.model.parcel;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

@Slf4j
@Getter
@Setter
public class Parcel {

    private String name;
    private char[][] form;
    private String symbol;
    private int height;
    private int length;
    private int area;

    private Parcel(Builder builder) {
        this.name = builder.name;
        this.form = builder.form;
        this.symbol = builder.symbol;
        this.height = builder.height;
        this.length = builder.length;
        this.area = builder.area;
    }

    public static class Builder {
        private String name;
        private char[][] form;
        private String symbol;
        private int height;
        private int length;
        private int area;

        public Builder setName(String name) {
            this.name = name;
            return this; // Возвращаем текущий объект Builder
        }

        public Builder setForm(String strForm) {
            String[] lines = strForm.split("\n");

            Collections.reverse(Arrays.asList(lines));

            this.height = lines.length;

            int maxLength = 0;
            for (String line : lines) {
                if (line.length() > maxLength) maxLength = line.length();
            }
            this.length = maxLength;
            char[][] arrForm = new char[height][length];
            for (int i = 0; i < arrForm.length; i++)
                for (int j = 0; j < arrForm[i].length; j++) {
                    arrForm[i][j] = ' ';
                }

            for (int i = 0; i < lines.length; i++)
                for (int j = 0; j < lines[i].length(); j++) {
                    arrForm[i][j] = lines[i].charAt(j);
                }

            this.area = 0;
            for (char[] chars : arrForm)
                for (char aChar : chars) {
                    if (aChar != ' ') this.area++;
                }
            this.form = arrForm;
            return this; // Возвращаем текущий объект Builder
        }

        public Builder setSymbol(String symbol) {
            this.symbol = symbol;
            return this; // Возвращаем текущий объект Builder
        }

        // Метод для создания объекта Parcel
        public Parcel build() {
            return new Parcel(this);
        }
    }

    public void changeSymbolTo(String newSymbol) {
        this.symbol = newSymbol;
        for (int i = 0; i < this.height; i++)
            for (int j = 0; j < this.length; j++) {
                if (form[i][j] != ' ') {
                    form[i][j] = newSymbol.charAt(0);
                }
            }
    }

    public void changeFormTo(char[][] newForm) {
        this.form = newForm;
    }

    public void reverseParcelForm() {
        int rows = this.height;
        int cols = this.length;
        char[][] rotated = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotated[i][j] = this.form[rows - 1 - i][j];
            }
        }

        this.form = rotated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parcel parcel = (Parcel) o;
        return height == parcel.height && length == parcel.length && area == parcel.area && Arrays.deepEquals(form, parcel.form);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(height, length, area);
        result = 31 * result + Arrays.deepHashCode(form);
        return result;
    }

    @Override
    public String toString() {
        String ls = System.lineSeparator();
        return "Parsel:" + ls + "name : " + name + ls + "form:" + Arrays.deepToString(form) + ls +
                "symbol: " + symbol + ls;
    }
}
