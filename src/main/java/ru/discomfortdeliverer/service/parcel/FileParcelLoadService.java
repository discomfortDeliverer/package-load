package ru.discomfortdeliverer.service.parcel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.discomfortdeliverer.exception.InvalidFilePathException;
import ru.discomfortdeliverer.exception.InvalidInputException;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.model.parcel.ParcelPart;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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

            Parcel.Builder parcelBuilder = new Parcel.Builder();
            ParcelPart parcelPart = ParcelPart.NAME;
            String lineSeparator = System.lineSeparator();
            for (String line : lines) {
                if (parcelPart.equals(ParcelPart.NAME) && !line.isEmpty()) {
                    parcelBuilder.setName(line);
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
                    parcelBuilder.setForm(form.toString());
                    form = new StringBuilder();
                    parcelPart = ParcelPart.SYMBOL;
                    continue;
                }
                if (parcelPart.equals(ParcelPart.SYMBOL) && !line.isEmpty()) {
                    parcelBuilder.setSymbol(line);
                    parcels.add(parcelBuilder.build());
                    parcelBuilder = new Parcel.Builder();
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

    public List<String> loadParcelNamesFromFileWithParcelNames(String filename) {
        log.info("Путь до файла - {}", filename);
        Path filePath = Paths.get(filename);

        List<Parcel> parcels = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(filePath);
            List<String> parcelNames = splitLinesWithParcelNames(lines);
            return parcelNames;
        } catch (IOException e) {
            log.error("Ошибка при загрузке имен посылок из файла - " + filename + " " + e.getMessage());
        }
        return null;
    }

    private List<String> splitLinesWithParcelNames(List<String> lines) {
        List<String> parcelNames = new ArrayList<>();
        for (String line : lines) {
            String[] names = line.split(" ");
            parcelNames.addAll(Arrays.asList(names));
        }

        return parcelNames;
    }
}
