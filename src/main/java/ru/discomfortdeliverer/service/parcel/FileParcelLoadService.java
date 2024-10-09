package ru.discomfortdeliverer.service.parcel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public List<String> loadParcelNamesFromFileWithParcelNames(String filename) {
        log.info("Путь до файла - {}", filename);
        Path filePath = Paths.get(filename);

        try {
            List<String> lines = Files.readAllLines(filePath);
            List<String> parcelNames = splitLinesWithParcelNames(lines);
            return parcelNames;
        } catch (IOException e) {
            log.error("Ошибка при загрузке имен посылок из файла - " + filename + " " + e.getMessage());
        }
        return null;
    }

    public List<String> splitLineWithParcelNames(String line) {
        String[] names = line.split(" ");
        return Arrays.asList(names);
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
