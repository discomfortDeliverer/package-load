package ru.discomfortdeliverer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.discomfortdeliverer.exception.InvalidFilePathException;
import ru.discomfortdeliverer.exception.InvalidInputException;
import ru.discomfortdeliverer.model.Parcel;
import ru.discomfortdeliverer.parcel.ParcelInputValidator;
import ru.discomfortdeliverer.parcel.ParcelPart;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FileParcelLoadService {
    /**
     * Загружает посылки из файла
     *
     * @param filename Принимает путь к файлу
     * @return Список с посылками
     * @throws InvalidInputException
     * @throws InvalidFilePathException
     */
    public List<Parcel> loadParcelsFromFile(String filename) {
        log.info("Путь до файла - {}", filename);
        Path filePath = Paths.get(filename);

        List<Parcel> parcels = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(filePath);
            log.debug("Полученные строки из файла - {}", lines);

            StringBuilder form = new StringBuilder();

            Parcel parcel = new Parcel();
            ParcelPart parcelPart = ParcelPart.NAME;
            String lineSeparator = System.lineSeparator();
            for (String line : lines) {
                if (parcelPart.equals(ParcelPart.NAME) && !line.isEmpty()) {
                    parcel.setName(line);
                    continue;
                }
                if (parcelPart.equals(ParcelPart.NAME) && line.isEmpty()) {
                    parcelPart = ParcelPart.FORM;
                    continue;
                }
                if (parcelPart.equals(ParcelPart.FORM) && !line.isEmpty()) {
                    form.append(line);
                    form.append(lineSeparator);
                    continue;
                }
                if (parcelPart.equals(ParcelPart.FORM) && line.isEmpty()) {
                    parcel.setFormFromString(form.toString());
                    form = new StringBuilder();
                    parcelPart = ParcelPart.SYMBOL;
                    continue;
                }
                if (parcelPart.equals(ParcelPart.SYMBOL) && !line.isEmpty()) {
                    parcel.setSymbol(line);
                    parcels.add(parcel);
                    parcel = new Parcel();
                    continue;
                }
                if (parcelPart.equals(ParcelPart.SYMBOL) && line.isEmpty()) {
                    parcelPart = ParcelPart.NAME;
                }
            }

        } catch (IOException e) {
            log.error("По пути - {} файл не найден", filePath);
            throw new InvalidFilePathException(e);
        }
        log.debug("Список с посылками из файла - {}", parcels);
        return parcels;
    }
}
