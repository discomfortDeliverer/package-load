package ru.discomfortdeliverer.model.truck;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.discomfortdeliverer.model.truck.Truck;

import java.util.List;

public class TruckList {
    @JsonProperty("trucks")
    private List<Truck> trucks;

    public List<Truck> getTrucks() {
        return trucks;
    }

    public void setTrucks(List<Truck> trucks) {
        this.trucks = trucks;
    }
}
