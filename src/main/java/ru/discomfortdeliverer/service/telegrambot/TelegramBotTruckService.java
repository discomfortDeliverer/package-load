package ru.discomfortdeliverer.service.telegrambot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.model.truck.Truck;
import ru.discomfortdeliverer.service.parcel.ParcelService;
import ru.discomfortdeliverer.service.truck.OptimalTruckLoader;
import ru.discomfortdeliverer.service.truck.SimpleTruckLoader;
import ru.discomfortdeliverer.service.truck.UniformTruckLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramBotTruckService {

    private final ObjectMapper objectMapper;
    private final ParcelService parcelService;
    private final SimpleTruckLoader simpleTruckLoader;
    private final OptimalTruckLoader optimalTruckLoader;
    private final UniformTruckLoader uniformTruckLoader;

    public String simpleLoad(String[] parcelNames) {
        List<String> parcelNamesList = new ArrayList<>(Arrays.asList(parcelNames));

        List<Parcel> parcels = parcelService.findParcelsByNames(parcelNamesList);
        List<Truck> trucks = simpleTruckLoader.loadParcels(parcels);

        try {
            String jsonResult = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(trucks);
            return "Результат погрузки простым методом:\n\n" + jsonResult;
        } catch (JsonProcessingException e) {
            log.error("Невозможно конвертировать trucks={} в json", trucks);
            throw new RuntimeException(e);
        }
    }

    public String optimalLoad(String[] parcelNames, String truckSize, String maxTrucks) {
        List<String> parcelNamesList = new ArrayList<>(Arrays.asList(parcelNames));

        List<Parcel> parcelsByNames = parcelService.findParcelsByNames(parcelNamesList);
        List<Truck> trucks = optimalTruckLoader.loadParcels(parcelsByNames, truckSize, maxTrucks);

        try {
            String jsonResult = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(trucks);
            return "Результат погрузки простым методом:\n\n" + jsonResult;
        } catch (JsonProcessingException e) {
            log.error("Невозможно конвертировать trucks={} в json", trucks);
            throw new RuntimeException(e);
        }
    }

    public String uniformLoad(String[] parcelNames, String truckSize, String maxTrucks) {
        List<String> parcelNamesList = new ArrayList<>(Arrays.asList(parcelNames));

        List<Parcel> parcelsByNames = parcelService.findParcelsByNames(parcelNamesList);
        List<Truck> trucks = uniformTruckLoader.loadParcels(parcelsByNames, truckSize, maxTrucks);
        try {
            String jsonResult = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(trucks);
            return "Результат погрузки простым методом:\n\n" + jsonResult;
        } catch (JsonProcessingException e) {
            log.error("Невозможно конвертировать trucks={} в json", trucks);
            throw new RuntimeException(e);
        }
    }
}
