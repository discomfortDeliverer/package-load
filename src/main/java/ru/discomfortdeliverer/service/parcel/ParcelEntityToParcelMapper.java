package ru.discomfortdeliverer.service.parcel;

import org.springframework.stereotype.Component;
import ru.discomfortdeliverer.entity.ParcelEntity;
import ru.discomfortdeliverer.model.parcel.Parcel;

import java.util.ArrayList;
import java.util.List;

@Component
public class ParcelEntityToParcelMapper {
    public Parcel mapParcelEntityToParcel(ParcelEntity parcelEntity) {
        Parcel parcel = new Parcel.Builder()
                .setName(parcelEntity.getName())
                .setFormFromString(parcelEntity.getForm())
                .setSymbol(parcelEntity.getSymbol())
                .build();
        return parcel;
    }

    public List<Parcel> mapParcelEntityListToParcelList(List<ParcelEntity> parcelEntities) {
        List<Parcel> parcels = new ArrayList<>();

        for (ParcelEntity parcelEntity : parcelEntities) {
            Parcel parcel = mapParcelEntityToParcel(parcelEntity);
            parcels.add(parcel);
        }
        return parcels;
    }
}
