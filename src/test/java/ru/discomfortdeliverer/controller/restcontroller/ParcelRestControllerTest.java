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
import ru.discomfortdeliverer.exception.UnableUpdateParcelException;
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

        List<Parcel> allParcels = parcelRestController.getAll();

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

        Parcel parcel = parcelRestController.getByName("Стол");

        assertThat(parcel).isNotNull();
        assertThat(parcel.getName()).isEqualTo("Стол");
        assertThat(parcel.getSymbol()).isEqualTo("$");
    }

    @Test
    public void getParcelByName_ShouldThrowParcelNotFoundException() {
        when(parcelRepository.findByName(Mockito.anyString())).thenReturn(null);


        ParcelNotFoundException exception = assertThrows(ParcelNotFoundException.class, () -> {
            parcelRestController.getByName("Стол");
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

        Parcel parcel = parcelRestController.deleteByName("Стол");

        assertThat(parcel.getName()).isEqualTo("Стол");
        assertThat(parcel.getSymbol()).isEqualTo("$");
    }

    @Test
    public void deleteParcelByName_ShouldThrowParcelNotFoundException() {
        when(parcelRepository.findByName(Mockito.anyString())).thenReturn(null);

        ParcelNotFoundException exception = assertThrows(ParcelNotFoundException.class, () -> {
            parcelRestController.deleteByName("Стол");
        });
        assertThat(exception.getMessage()).isEqualTo("Посылка с именем - Стол не найдена");
    }

    @Test
    public void changeParcelNameByName_ShouldReturnParcelWithChangedName() {
        ParcelEntity parcelEntity = new ParcelEntity();
        parcelEntity.setName("Стул");
        parcelEntity.setForm("$$$$$$$\n   $   \n   $   \n   $   ");
        parcelEntity.setSymbol("$");
        when(parcelRepository.updateParcelNameByName(Mockito.anyString(), Mockito.anyString())).thenReturn(1);
        when(parcelRepository.findByName(Mockito.anyString())).thenReturn(parcelEntity);


        Parcel updatedParcel = parcelRestController.changeNameByName("Стол", "Стул");

        assertThat(updatedParcel).isNotNull();
        assertThat(updatedParcel.getName()).isEqualTo("Стул");
        assertThat(updatedParcel.getSymbol()).isEqualTo("$");
    }

    @Test
    public void changeParcelNameByName_ShouldThrowUnableUpdateParcelException() {
        when(parcelRepository.updateParcelNameByName(Mockito.anyString(), Mockito.anyString())).thenReturn(0);

        UnableUpdateParcelException exception = assertThrows(UnableUpdateParcelException.class, () -> {
            parcelRestController.changeNameByName("Стол", "Стул");
        });
        assertThat(exception.getMessage()).isEqualTo("Невозможно обновить посылку с именем - Стол");
    }

    @Test
    public void changeParcelFormByName_ShouldChangeSymbolAndForm() {
        ParcelEntity newParcelEntity = new ParcelEntity();
        newParcelEntity.setName("Стул");
        newParcelEntity.setForm("### \n #  \n####");
        newParcelEntity.setSymbol("#");
        when(parcelRepository.updateParcelByName(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(1);
        when(parcelRepository.findByName(Mockito.anyString())).thenReturn(newParcelEntity);


        Parcel updatedParcel = parcelRestController.changeFormByName(newParcelEntity);

        assertThat(updatedParcel).isNotNull();
        assertThat(updatedParcel.getName()).isEqualTo("Стул");
        assertThat(updatedParcel.getSymbol()).isEqualTo("#");
        assertThat(updatedParcel.getForm()).isEqualTo(new char[][]{
                {'#', '#', '#', ' '},
                {' ', '#', ' ', ' '},
                {'#', '#', '#', '#'}
        });
    }

    @Test
    public void changeParcelFormByName_ShouldThrowUnableUpdateParcelException() {
        ParcelEntity newParcelEntity = new ParcelEntity();
        newParcelEntity.setName("Стул");
        newParcelEntity.setForm("### \n #  \n####");
        newParcelEntity.setSymbol("#");
        when(parcelRepository.updateParcelNameByName(Mockito.anyString(), Mockito.anyString())).thenReturn(0);

        UnableUpdateParcelException exception = assertThrows(UnableUpdateParcelException.class, () -> {
            parcelRestController.changeFormByName(newParcelEntity);
        });
        assertThat(exception.getMessage()).isEqualTo("Невозможно обновить форму посылки с именем - Стул");
    }

    @Test
    public void changeParcelSymbolByName_ShouldChangeSymbolAndForm() {
        ParcelEntity newParcelEntity = new ParcelEntity();
        newParcelEntity.setName("Стул");
        newParcelEntity.setForm("### \n #  \n####");
        newParcelEntity.setSymbol("#");
        when(parcelRepository.findByName(Mockito.anyString())).thenReturn(newParcelEntity);
        when(parcelRepository.updateParcelSymbolByName(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(1);

        Parcel updatedParcel = parcelRestController.changeSymbolByName("Стул", "%");

        assertThat(updatedParcel).isNotNull();
        assertThat(updatedParcel.getName()).isEqualTo("Стул");
        assertThat(updatedParcel.getSymbol()).isEqualTo("%");
        assertThat(updatedParcel.getForm()).isEqualTo(new char[][]{
                {'%', '%', '%', ' '},
                {' ', '%', ' ', ' '},
                {'%', '%', '%', '%'}
        });
    }

    @Test
    public void changeParcelSymbolByName_ShouldThrowParcelNotFoundException() {
        when(parcelRepository.findByName(Mockito.anyString())).thenReturn(null);

        ParcelNotFoundException exception = assertThrows(ParcelNotFoundException.class, () -> {
            parcelRestController.changeSymbolByName("Стул", "$");
        });
        assertThat(exception.getMessage()).isEqualTo("Посылка с именем - Стул не найдена");
    }
}
