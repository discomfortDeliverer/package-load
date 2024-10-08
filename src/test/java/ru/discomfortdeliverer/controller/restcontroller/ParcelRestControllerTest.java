package ru.discomfortdeliverer.controller.restcontroller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.discomfortdeliverer.entity.ParcelEntity;
import ru.discomfortdeliverer.exception.ParcelNotFoundException;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.repository.ParcelRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
public class ParcelRestControllerTest {
    @MockBean
    private ParcelRepository parcelRepository;
    @Autowired
    private ParcelRestController parcelRestController;
    private List<ParcelEntity> parcelEntities;

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
    public void getAllParcels_ShouldReturnAllParcels() {
        when(parcelRepository.findAll()).thenReturn(parcelEntities);

        List<Parcel> allParcels = parcelRestController.getAllParcels();

        assertThat(allParcels.size()).isEqualTo(3);
        assertThat(allParcels.get(0).getName()).isEqualTo("Штанга");
        assertThat(allParcels.get(1).getName()).isEqualTo("Кружка");
        assertThat(allParcels.get(2).getName()).isEqualTo("Стол");
    }

    @Test
    public void getParcelByName_ShouldReturnParcelByName() {
        ParcelEntity parcelEntity = new ParcelEntity();
        parcelEntity.setName("Стол");
        parcelEntity.setForm("$$$$$$$\n   $   \n   $   \n   $   ");
        parcelEntity.setSymbol("$");
        when(parcelRepository.findByName(Mockito.anyString())).thenReturn(parcelEntity);

        Parcel parcel = parcelRestController.getParcelByName("Стол");

        assertThat(parcel).isNotNull();
        assertThat(parcel.getName()).isEqualTo("Стол");
        assertThat(parcel.getSymbol()).isEqualTo("$");
    }

    @Test
    public void getParcelByName_ShouldThrowParcelNotFoundException() {
        when(parcelRepository.findByName(Mockito.anyString())).thenReturn(null);


        ParcelNotFoundException exception = assertThrows(ParcelNotFoundException.class, () -> {
            parcelRestController.getParcelByName("Стол");
        });
        assertThat(exception.getMessage()).isEqualTo("Посылка с именем - Стол не найдена");
    }

    @Test
    public void deleteParcelByName_ShouldReturnDeletedParcel() {
        ParcelEntity parcelEntity = new ParcelEntity();
        parcelEntity.setName("Стол");
        parcelEntity.setForm("$$$$$$$\n   $   \n   $   \n   $   ");
        parcelEntity.setSymbol("$");
        when(parcelRepository.findByName(Mockito.anyString())).thenReturn(parcelEntity);

        Parcel parcel = parcelRestController.deleteParcelByName("Стол");

        assertThat(parcel.getName()).isEqualTo("Стол");
        assertThat(parcel.getSymbol()).isEqualTo("$");
    }

    @Test
    public void deleteParcelByName_ShouldThrowParcelNotFoundException() {
        when(parcelRepository.findByName(Mockito.anyString())).thenReturn(null);

        ParcelNotFoundException exception = assertThrows(ParcelNotFoundException.class, () -> {
            parcelRestController.deleteParcelByName("Стол");
        });
        assertThat(exception.getMessage()).isEqualTo("Посылка с именем - Стол не найдена");
    }
}
