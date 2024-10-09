package ru.discomfortdeliverer.controller.restcontroller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.discomfortdeliverer.entity.ParcelEntity;
import ru.discomfortdeliverer.exception.ParcelNotFoundException;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.model.truck.Truck;
import ru.discomfortdeliverer.model.truck.TruckParcelsCounter;
import ru.discomfortdeliverer.model.truck.TruckParcelsCounterWrapper;
import ru.discomfortdeliverer.repository.ParcelRepository;
import ru.discomfortdeliverer.service.parcel.ParcelEntityToParcelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
public class TruckRestControllerTest {

    @MockBean
    private ParcelRepository parcelRepository;
    @Autowired
    private TruckRestController truckRestController;
    private List<ParcelEntity> parcelEntities;
    @Autowired
    private ParcelEntityToParcelMapper parcelEntityToParcelMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        parcelEntities = new ArrayList<>();
        ParcelEntity parcelEntity = new ParcelEntity();
        parcelEntity.setName("Штанга");
        parcelEntity.setForm("@     @\n@@@@@@@\n@     @");
        parcelEntity.setSymbol("@");
        parcelEntities.add(parcelEntity);

        parcelEntity = new ParcelEntity();
        parcelEntity.setName("Кружка");
        parcelEntity.setForm("###### \n#    ##\n#    ##\n###### ");
        parcelEntity.setSymbol("#");
        parcelEntities.add(parcelEntity);

        parcelEntity = new ParcelEntity();
        parcelEntity.setName("Стол");
        parcelEntity.setForm("$$$$$$$\n   $   \n   $   \n   $   ");
        parcelEntity.setSymbol("$");
        parcelEntities.add(parcelEntity);
    }

    @Test
    public void countParcelsFromTrucks_ShouldCountCorrect() {
        // Arrange
        Truck truck = new Truck(10, 8);
        Parcel barbell = parcelEntityToParcelMapper.mapParcelEntityToParcel(parcelEntities.get(0));
        Parcel cup = parcelEntityToParcelMapper.mapParcelEntityToParcel(parcelEntities.get(1));
        Parcel table = parcelEntityToParcelMapper.mapParcelEntityToParcel(parcelEntities.get(2));
        truck.placeParcelByCoordinates(table, 0, 0);
        truck.placeParcelByCoordinates(barbell, 4, 0);

        Truck truck2 = new Truck(10, 16);
        truck2.placeParcelByCoordinates(cup, 0, 0);
        truck2.placeParcelByCoordinates(cup, 4, 0);
        truck2.placeParcelByCoordinates(barbell, 0, 7);
        truck2.placeParcelByCoordinates(table, 3, 7);

        List<Truck> trucks = List.of(truck, truck2);

        when(parcelRepository.findAll()).thenReturn(parcelEntities);

        // Action
        TruckParcelsCounterWrapper truckParcelsCounterWrapper = truckRestController.countParcelsFromTrucks(trucks);

        // Assert
        assertThat(truckParcelsCounterWrapper).isNotNull();
        List<TruckParcelsCounter> truckParcelsCounters = truckParcelsCounterWrapper.getTruckParcelsCounters();
        assertThat(truckParcelsCounters.size()).isEqualTo(2);

        TruckParcelsCounter truckParcelsCounter = truckParcelsCounters.get(0);
        Map<Parcel, Integer> firstTruck = truckParcelsCounter.getParcelsAndCount();
        assertThat(firstTruck.containsKey(table)).isTrue();
        assertThat(firstTruck.get(table)).isEqualTo(1);
        assertThat(firstTruck.containsKey(barbell)).isTrue();
        assertThat(firstTruck.get(barbell)).isEqualTo(1);

        truckParcelsCounter = truckParcelsCounters.get(1);
        Map<Parcel, Integer> secondTruck = truckParcelsCounter.getParcelsAndCount();
        assertThat(secondTruck.containsKey(cup)).isTrue();
        assertThat(secondTruck.get(cup)).isEqualTo(2);
        assertThat(secondTruck.containsKey(barbell)).isTrue();
        assertThat(secondTruck.get(barbell)).isEqualTo(1);
        assertThat(secondTruck.containsKey(table)).isTrue();
        assertThat(secondTruck.get(table)).isEqualTo(1);
    }

    @Test
    public void loadBySimpleRule_ShouldLoadEveryParcel() {
        when(parcelRepository.findAll()).thenReturn(parcelEntities);
        when(parcelRepository.findByName("Штанга")).thenReturn(parcelEntities.get(0));
        when(parcelRepository.findByName("Кружка")).thenReturn(parcelEntities.get(1));
        when(parcelRepository.findByName("Стол")).thenReturn(parcelEntities.get(2));

        List<Truck> trucks = truckRestController.loadBySimpleRule("Штанга Кружка Стол Стол Кружка Кружка");

        assertThat(trucks.size()).isEqualTo(6);
    }

    @Test
    public void loadBySimpleRule_ShouldThrowException() {
        when(parcelRepository.findAll()).thenReturn(parcelEntities);
        when(parcelRepository.findByName("Штанга")).thenReturn(parcelEntities.get(0));
        when(parcelRepository.findByName("Кружка")).thenReturn(parcelEntities.get(1));
        when(parcelRepository.findByName("Стол")).thenReturn(null);

        ParcelNotFoundException exception = assertThrows(ParcelNotFoundException.class, () -> {
            truckRestController.loadBySimpleRule("Штанга Кружка Стол Стол Кружка");
        });
        assertThat(exception.getMessage()).isEqualTo("Посылка с именем - Стол не найдена");
    }

    @Test
    public void loadByOptimalRule_ShouldLoadEveryParcel() {
        when(parcelRepository.findAll()).thenReturn(parcelEntities);
        when(parcelRepository.findByName("Штанга")).thenReturn(parcelEntities.get(0));
        when(parcelRepository.findByName("Кружка")).thenReturn(parcelEntities.get(1));
        when(parcelRepository.findByName("Стол")).thenReturn(parcelEntities.get(2));

        List<Truck> trucks = truckRestController.loadByOptimalRule("Штанга Кружка Стол Стол Кружка Кружка",
                "8x8", "3");

        assertThat(trucks.size()).isEqualTo(3);
        assertThat(trucks.get(0).getTruckLength()).isEqualTo(8);
        assertThat(trucks.get(0).getTruckHeight()).isEqualTo(8);
        assertThat(trucks.get(1).getTruckLength()).isEqualTo(8);
        assertThat(trucks.get(1).getTruckHeight()).isEqualTo(8);
        assertThat(trucks.get(2).getTruckLength()).isEqualTo(8);
        assertThat(trucks.get(2).getTruckHeight()).isEqualTo(8);
    }

    @Test
    public void loadByOptimalRule_ShouldThrowException() {
        when(parcelRepository.findAll()).thenReturn(parcelEntities);
        when(parcelRepository.findByName("Штанга")).thenReturn(parcelEntities.get(0));
        when(parcelRepository.findByName("Кружка")).thenReturn(parcelEntities.get(1));
        when(parcelRepository.findByName("Стол")).thenReturn(null);

        ParcelNotFoundException exception = assertThrows(ParcelNotFoundException.class, () -> {
            truckRestController.loadByOptimalRule("Штанга Кружка Стол Стол Кружка",
                    "8x8", "3");
        });
        assertThat(exception.getMessage()).isEqualTo("Посылка с именем - Стол не найдена");
    }
    @Test
    public void loadByUniformRule_ShouldLoadEveryParcel() {
        when(parcelRepository.findAll()).thenReturn(parcelEntities);
        when(parcelRepository.findByName("Штанга")).thenReturn(parcelEntities.get(0));
        when(parcelRepository.findByName("Кружка")).thenReturn(parcelEntities.get(1));
        when(parcelRepository.findByName("Стол")).thenReturn(parcelEntities.get(2));

        List<Truck> trucks = truckRestController.loadByUniformRule("Штанга Кружка Стол Стол Кружка Кружка",
                "8x8", "3");

        assertThat(trucks.size()).isEqualTo(3);
        assertThat(trucks.get(0).getTruckLength()).isEqualTo(8);
        assertThat(trucks.get(0).getTruckHeight()).isEqualTo(8);
        assertThat(trucks.get(1).getTruckLength()).isEqualTo(8);
        assertThat(trucks.get(1).getTruckHeight()).isEqualTo(8);
        assertThat(trucks.get(2).getTruckLength()).isEqualTo(8);
        assertThat(trucks.get(2).getTruckHeight()).isEqualTo(8);
    }

    @Test
    public void loadByUniformRule_ShouldThrowException() {
        when(parcelRepository.findAll()).thenReturn(parcelEntities);
        when(parcelRepository.findByName("Штанга")).thenReturn(parcelEntities.get(0));
        when(parcelRepository.findByName("Кружка")).thenReturn(parcelEntities.get(1));
        when(parcelRepository.findByName("Стол")).thenReturn(null);

        ParcelNotFoundException exception = assertThrows(ParcelNotFoundException.class, () -> {
            truckRestController.loadByUniformRule("Штанга Кружка Стол Стол Кружка",
                    "8x8", "3");
        });
        assertThat(exception.getMessage()).isEqualTo("Посылка с именем - Стол не найдена");
    }
}
