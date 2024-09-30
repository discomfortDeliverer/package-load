package ru.discomfortdeliverer.service.truck;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import ru.discomfortdeliverer.model.truck.Truck;
import ru.discomfortdeliverer.model.truck.TruckList;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class FileTruckLoadService {
    /**
     * Загружает грузовик из Json файла
     *
     * @param filepath Путь к файлу
     * @return Возвращает объект Truck, загруженный из файла
     */
    public Truck loadTruckFromJsonFile(String filepath) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(new File(filepath), Truck.class);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла: " + filepath, e);
        }
    }

    /**
     * Загружает список грузовиков из Json файла
     *
     * @param filepath Путь к файлу
     * @return Список загруженных грузовиков
     */
    public List<Truck> loadTrucksFromJsonFile(String filepath) {
        ObjectMapper objectMapper = new ObjectMapper();
        Path filePath = Paths.get(filepath);

        try (FileReader fileReader = new FileReader(filepath)) {

            TruckList truckList = objectMapper.readValue(new File(filepath), TruckList.class);
            return truckList.getTrucks();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
