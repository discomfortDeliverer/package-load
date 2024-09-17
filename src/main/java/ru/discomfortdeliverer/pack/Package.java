package ru.discomfortdeliverer.pack;

import java.util.ArrayList;
import java.util.List;

public class Package {
    private int area;
    private char character;
    private List<Integer> lineLengths = new ArrayList<>();

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public List<Integer> getLineLengths() {
        return lineLengths;
    }

    public void setLineLengths(List<Integer> lineLengths) {
        this.lineLengths = lineLengths;
    }

    public void addLineLength(int lineLength){
        lineLengths.add(lineLength);
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }
}
