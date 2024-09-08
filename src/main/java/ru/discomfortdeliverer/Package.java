package ru.discomfortdeliverer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Package {
    private int area;
    private char character;
    private List<Integer> linelengths = new ArrayList<>();

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public List<Integer> getLinelengths() {
        return linelengths;
    }

    public void setLinelengths(List<Integer> linelengths) {
        this.linelengths = linelengths;
    }

    public void addLineLength(int lineLength){
        linelengths.add(lineLength);
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }
}
