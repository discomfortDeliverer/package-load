package ru.discomfortdeliverer.model.truck;

import lombok.Getter;
import lombok.Setter;
import ru.discomfortdeliverer.model.parcel.Parcel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
public class TruckParcelsCounter {

    private final Map<Parcel, Integer> parcelsAndCount;

    public TruckParcelsCounter() {
        this.parcelsAndCount = new HashMap<>();
    }

    public void addParcelAndCount(Map<Parcel, Integer> newParcelsAndCount) {
        for (Map.Entry<Parcel, Integer> entry : newParcelsAndCount.entrySet()) {
            Parcel parcel = entry.getKey();
            Integer addCount = entry.getValue();
            if (parcelsAndCount.containsKey(parcel)) {
                Integer count = parcelsAndCount.get(parcel);
                count += addCount;
                parcelsAndCount.put(parcel, count);
            } else {
                parcelsAndCount.put(parcel, addCount);
            }
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TruckParcelsCounter that = (TruckParcelsCounter) o;
        return Objects.equals(parcelsAndCount, that.parcelsAndCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parcelsAndCount);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<Parcel, Integer> entry : parcelsAndCount.entrySet()) {
            stringBuilder.append("\n")
                    .append(entry.getKey())
                    .append("Количество в грузовике: ")
                    .append(entry.getValue())
                    .append("\n");
        }

        return stringBuilder.toString();
    }
}
