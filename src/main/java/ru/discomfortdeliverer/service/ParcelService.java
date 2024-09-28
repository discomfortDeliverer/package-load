package ru.discomfortdeliverer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.discomfortdeliverer.model.Parcel;
import ru.discomfortdeliverer.repository.ParcelRepository;

import java.util.Arrays;
import java.util.List;

@Service
public class ParcelService {
    private final ParcelRepository parcelRepository;

    @Autowired
    public ParcelService(ParcelRepository parcelRepository) {
        this.parcelRepository = parcelRepository;
    }

    public List<Parcel> getAllParcels() {
        return parcelRepository.getAllParcels();
    }

    public Parcel changeSymbol(String parcelName, String newSymbol) {
        return parcelRepository.changeSymbol(parcelName, newSymbol);
    }

    public Parcel changeParcelForm(String parcelName, String newForm) {
        String[] split = newForm.split("\n");
        int maxLength = 0;
        for (String line : split) {
            if (line.length() > maxLength) {
                maxLength = line.length();
            }
        }

        char[][] form = new char[split.length][maxLength];
        for (char[] chars : form) {
            Arrays.fill(chars, ' ');
        }

        for (int i = 0; i < form.length; i++) {
            String line = split[i];
            for (int j = 0; j < form[i].length; j++) {
                form[i][j] = line.charAt(j);
            }
        }

        return parcelRepository.changeParcelForm(parcelName, form);
    }
}
