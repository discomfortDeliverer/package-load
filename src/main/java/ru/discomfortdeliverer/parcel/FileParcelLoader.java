package ru.discomfortdeliverer.parcel;

import ru.discomfortdeliverer.exception.InvalidFilePathException;
import ru.discomfortdeliverer.exception.InvalidInputException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileParcelLoader {
    private ParcelInputValidator parcelInputValidator;

    public FileParcelLoader(ParcelInputValidator parcelInputValidator) {
        this.parcelInputValidator = parcelInputValidator;
    }

    public List<Parcel> loadParcelsFromFile(String filename) throws InvalidInputException, InvalidFilePathException {
        Path filePath = Paths.get(filename);

        List<Parcel> parcels = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(filePath);

            if (!parcelInputValidator.isValidListOfLines(lines)) {
                throw new InvalidInputException();
            }
            StringBuilder stringBuilder = new StringBuilder();

            for (String line : lines) {
                if(!line.equals("")) {
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
            throw new InvalidFilePathException(e);
        }

        return parcels;
    }
}
