package ru.discomfortdeliverer.parcel;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class Parcel {
    private int height;
    private int length;
    private char[][] body;
    private int area;

    public Parcel(String inputParcel) {
        String[] lines = inputParcel.split("\n");

        Collections.reverse(Arrays.asList(lines));

        this.height = lines.length;

        int maxLength = 0;
        for (String line : lines) {
            if (line.length() > maxLength) maxLength = line.length();
        }
        length = maxLength;
        body = new char[height][length];
        for (int i = 0; i < body.length; i++)
            for (int j = 0; j < body[i].length; j++) {
                body[i][j] = ' ';
            }

        for (int i = 0; i < lines.length; i++)
            for (int j = 0; j < lines[i].length(); j++) {
                body[i][j] = lines[i].charAt(j);
            }

        this.area = 0;
        for (char[] chars : body)
            for (char aChar : chars) {
                if (aChar != ' ') this.area++;
            }
    }

    public int getHeight() {
        return height;
    }

    public int getLength() {
        return length;
    }

    public char[][] getBody() {
        return body;
    }

    public int getArea() {
        return area;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parcel parcel = (Parcel) o;
        return height == parcel.height && length == parcel.length && area == parcel.area && Arrays.deepEquals(body, parcel.body);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(height, length, area);
        result = 31 * result + Arrays.hashCode(body);
        return result;
    }
}
