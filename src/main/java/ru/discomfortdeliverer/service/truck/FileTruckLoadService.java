package ru.discomfortdeliverer.service.truck;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.discomfortdeliverer.model.truck.Truck;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class FileTruckLoadService {

    private final ObjectMapper objectMapper;

    /**
     * Загружает грузовик из Json файла
     *
     * @param filePath Путь к файлу
     * @return Возвращает объект Truck, загруженный из файла
     */
    public Truck loadTruckFromJsonFile(String filePath) {
        try {
            return objectMapper.readValue(new File(filePath), Truck.class);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла: " + filePath, e);
        }
    }

    /**
     * Загружает список грузовиков из Json файла
     *
     * @param filepath Путь к файлу
     * @return Список загруженных грузовиков
     */
    public List<Truck> loadTrucksFromJsonFile(String filepath) {
        log.info("Вызван метод loadTrucksFromJsonFile, filepath={}", filepath);
        Path filePath = Paths.get(filepath);

        try (FileReader fileReader = new FileReader(filepath)) {

            List<Truck> trucks = objectMapper.readValue(new File(filepath), new TypeReference<List<Truck>>() {
            });
            log.info("Прочитан список объектов из Json-файла truckList={}", trucks);
            return trucks;
        } catch (IOException e) {
            log.error("Ошибка чтения файла по пути - {}", filepath);
            throw new RuntimeException(e);
        }
    }
}
