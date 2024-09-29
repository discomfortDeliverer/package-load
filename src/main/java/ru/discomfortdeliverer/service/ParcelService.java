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

    public Parcel changeParcelForm(String parcelName, String newForm, String symbol) {
        String[] split = newForm.split("n");
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
            for (int j = 0; j < line.length(); j++) {
                form[i][j] = line.charAt(j);
            }
        }

        reverseForm(form);
        return parcelRepository.changeParcelForm(parcelName, form, symbol);
    }

    private void reverseForm(char[][] form) {
        for (int i = 0; i < form.length / 2; i++) {
            // Меняем местами строки
            char[] temp = form[i];
            form[i] = form[form.length - 1 - i];
            form[form.length - 1 - i] = temp;
        }
    }

    public Parcel showParcelByName(String parcelName) {
        return parcelRepository.findParcelByName(parcelName);
    }

    public Parcel deleteParcelByName(String parcelName) {
        return parcelRepository.deleteParcelByName(parcelName);
    }
}
