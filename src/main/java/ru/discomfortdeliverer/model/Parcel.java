package ru.discomfortdeliverer.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class Parcel {
    private String name;
    private char[][] form;
    private String symbol;
    private int height;
    private int length;
    private int area;

    public void setFormFromString(String inputParcel) {
        String lineSeparator = System.lineSeparator();
        String[] lines = inputParcel.split(lineSeparator);

        Collections.reverse(Arrays.asList(lines));

        this.height = lines.length;

        int maxLength = 0;
        for (String line : lines) {
            if (line.length() > maxLength) maxLength = line.length();
        }
        length = maxLength;
        form = new char[height][length];
        for (int i = 0; i < form.length; i++)
            for (int j = 0; j < form[i].length; j++) {
                form[i][j] = ' ';
            }

        for (int i = 0; i < lines.length; i++)
            for (int j = 0; j < lines[i].length(); j++) {
                form[i][j] = lines[i].charAt(j);
            }

        this.area = 0;
        for (char[] chars : form)
            for (char aChar : chars) {
                if (aChar != ' ') this.area++;
            }
        log.debug("Создан объект Parcel, height={}, length={}, form={}, area={}", height, length, form, area);
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
        return "Parsel:\n" + "name : " + name + "\n" + "form:" + Arrays.deepToString(form) + "\n" +
                "symbol: " + symbol + "\n";
    }
}
