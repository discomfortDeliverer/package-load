package ru.discomfortdeliverer.service.parcel;

import org.springframework.stereotype.Component;
import ru.discomfortdeliverer.entity.ParcelEntity;
import ru.discomfortdeliverer.model.parcel.Parcel;

@Component
public class ParcelToParcelEntityMapper {
    public ParcelEntity mapParcelToParcelEntity(Parcel parcel) {
        ParcelEntity parcelEntity = new ParcelEntity();
        parcelEntity.setName(parcel.getName());
        parcelEntity.setSymbol(parcel.getSymbol());

        char[][] form = parcel.getForm();
        form = reverseParcelForm(form, parcel.getHeight(), parcel.getLength());
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < form.length; i++)
            for (int j = 0; j < form[i].length; j++) {
                stringBuilder.append(form[i][j]);
                if (j == form[i].length - 1 && i != form.length - 1) {
                    stringBuilder.append(System.lineSeparator());
                }
            }
        parcelEntity.setForm(stringBuilder.toString());

        return parcelEntity;
    }

    private char[][] reverseParcelForm(char[][] form, int height, int length) {
        char[][] rotated = new char[height][length];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++) {
                rotated[i][j] = form[height - 1 - i][j];
            }
        }
        return rotated;
    }
}
