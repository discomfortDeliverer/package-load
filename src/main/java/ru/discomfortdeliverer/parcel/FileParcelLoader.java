package ru.discomfortdeliverer.parcel;

import lombok.extern.slf4j.Slf4j;
import ru.discomfortdeliverer.exception.InvalidFilePathException;
import ru.discomfortdeliverer.exception.InvalidInputException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FileParcelLoader {
    private ParcelInputValidator parcelInputValidator;

    public FileParcelLoader(ParcelInputValidator parcelInputValidator) {
        this.parcelInputValidator = parcelInputValidator;
    }

    public List<Parcel> loadParcelsFromFile(String filename) throws InvalidInputException, InvalidFilePathException {
        log.info("Путь до файла - {}", filename);
        Path filePath = Paths.get(filename);

        List<Parcel> parcels = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(filePath);
            log.debug("Полученные строки из файла - {}", lines);

            if (!parcelInputValidator.isValidListOfLines(lines)) {
                log.error("Файл по пути {} содержит невалидные данные", filePath);
                throw new InvalidInputException();
            }
            StringBuilder stringBuilder = new StringBuilder();

            for (String line : lines) {
                if (!line.equals("")) {
                    stringBuilder.append(line);
                    stringBuilder.append("\n");
                } else {
                    Parcel parcel = new Parcel(stringBuilder.toString());
                    parcels.add(parcel);
                    stringBuilder = new StringBuilder();
                }
            }

            if (!lines.get(lines.size() - 1).isEmpty()) {
                Parcel parcel = new Parcel(stringBuilder.toString());
                parcels.add(parcel);
            }

        } catch (IOException e) {
            log.error("По пути - {} файл не найден", filePath);
            throw new InvalidFilePathException(e);
        }
        log.debug("Список с посылками из файла - {}", parcels);
        return parcels;
    }
}
