package ru.discomfortdeliverer.truck;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileTruckLoader {
    public Truck loadTruckFromJsonFile(String filepath) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(new File(filepath), Truck.class);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла: " + filepath, e);
        }
    }
}
