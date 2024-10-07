package ru.discomfortdeliverer.service.parcel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.discomfortdeliverer.model.parcel.Parcel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class FileParcelSaveToFileService {
    /**
     * Метод сохраняет список с посылками в файл по указанному пути
     * @param filename Путь до файла, куда сохранить список посылок
     * @param parcels Список посылок, который надо сохранить
     */
    public void save(String filename, List<Parcel> parcels) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            String lineSeparator = System.lineSeparator();
            for (Parcel parcel : parcels) {
                writer.write(parcel.getName());
                writer.write(lineSeparator);
                writer.write(lineSeparator);

                char[][] form = parcel.getForm();
                for (int i = form.length - 1; i >= 0; i--) {
                    writer.write(form[i]);
                    writer.write(lineSeparator);
                }
                writer.write(lineSeparator);

                writer.write(parcel.getSymbol());
                writer.write(lineSeparator);
                writer.write(lineSeparator);

                writer.flush();
            }
            log.info("Данные успешно сохранены в файл " + filename);
        } catch (IOException e) {
            log.error("Ошибка при записи в файл: " + e.getMessage());
        }
    }
}
