package ru.discomfortdeliverer.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.discomfortdeliverer.exception.ParcelNotFoundException;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.repository.ParcelRepository;
import ru.discomfortdeliverer.service.parcel.FileParcelLoadService;
import ru.discomfortdeliverer.service.parcel.FileParcelSaveToFileService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class ParcelRepositoryTests {
//
//    @Mock
//    private FileParcelLoadService fileParcelLoadService;
//
//    @Mock
//    private FileParcelSaveToFileService fileParcelSaveToFileService;
//
//    @InjectMocks
//    private ParcelRepository parcelRepository;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        parcelRepository.setPathToRepositoryFile("/path");
//    }
//
//    @Test
//    public void testInitLoadsParcels() {
//        List<Parcel> mockParcels = Arrays.asList(
//                new Parcel("Штанга", new char[][] {
//                        {'@', ' ', ' ', ' ', ' ', ' ', '@'},
//                        {'@', '@', '@', '@', '@', '@', '@'},
//                        {'@', ' ', ' ', ' ', ' ', ' ', '@'}
//                }, "@", 3, 7, 11),
//                new Parcel("Табурет", new char[][] {
//                        {'#', '#', '#', '#'},
//                        {'#', ' ', ' ', '#'},
//                        {'#', ' ', ' ', '#'},
//                        {'#', ' ', ' ', '#'}
//                }, "#", 4, 4, 10));
//        when(fileParcelLoadService.loadParcelsFromFile("/path")).thenReturn(mockParcels);
//
//        parcelRepository.init();
//
//        assertEquals(2, parcelRepository.getAll().size());
//        assertEquals("Штанга", parcelRepository.getAll().get(0).getName());
//    }
//
//    @Test
//    public void testFindParcelByName() {
//        Parcel parcel = new Parcel("Штанга", new char[][] {
//                {'@', ' ', ' ', ' ', ' ', ' ', '@'},
//                {'@', '@', '@', '@', '@', '@', '@'},
//                {'@', ' ', ' ', ' ', ' ', ' ', '@'}
//        }, "@", 3, 7, 11);
//        parcelRepository.setParcels(Arrays.asList(parcel));
//
//        Parcel foundParcel = parcelRepository.findParcelByName("Штанга");
//
//        assertEquals("Штанга", foundParcel.getName());
//    }
//
//    @Test
//    public void testFindParcelByNameThrowsException() {
//        Parcel parcel = new Parcel("Штанга", new char[][] {
//                {'@', ' ', ' ', ' ', ' ', ' ', '@'},
//                {'@', '@', '@', '@', '@', '@', '@'},
//                {'@', ' ', ' ', ' ', ' ', ' ', '@'}
//        }, "@", 3, 7, 11);
//        parcelRepository.setParcels(Arrays.asList(parcel));
//
//        Exception exception = assertThrows(ParcelNotFoundException.class, () -> {
//            parcelRepository.findParcelByName("TestParcel");
//        });
//
//        assertEquals("Посылка с именем TestParcel не найдена", exception.getMessage());
//    }
//
//    @Test
//    public void testChangeSymbol() {
//        Parcel parcel = new Parcel("Штанга", new char[][] {
//                {'@', ' ', ' ', ' ', ' ', ' ', '@'},
//                {'@', '@', '@', '@', '@', '@', '@'},
//                {'@', ' ', ' ', ' ', ' ', ' ', '@'}
//        }, "@", 3, 7, 11);
//        parcelRepository.setParcels(Arrays.asList(parcel));
//
//        Parcel updatedParcel = parcelRepository.updateSymbol("Штанга", "%");
//
//        assertEquals("%", updatedParcel.getSymbol());
//    }
//
//    @Test
//    public void testDeleteParcelByName() {
//        Parcel parcel = new Parcel("Штанга", new char[][] {
//                {'@', ' ', ' ', ' ', ' ', ' ', '@'},
//                {'@', '@', '@', '@', '@', '@', '@'},
//                {'@', ' ', ' ', ' ', ' ', ' ', '@'}
//        }, "@", 3, 7, 11);
//        parcelRepository.setParcels(new ArrayList<>(Arrays.asList(parcel)));
//
//        Parcel deletedParcel = parcelRepository.deleteByName("Штанга");
//
//        assertEquals(parcel, deletedParcel);
//        assertEquals(0, parcelRepository.getAll().size());
//    }
//
//    @Test
//    public void testDeleteNonExistentParcelThrowsException() {
//        Exception exception = assertThrows(ParcelNotFoundException.class, () -> {
//            parcelRepository.deleteByName("NonExistent");
//        });
//
//        assertEquals("Посылка с именем NonExistent не найдена", exception.getMessage());
//    }
//
//    @Test
//    public void testChangeParcelName() {
//        Parcel parcel = new Parcel("Штанга", new char[][] {
//                {'@', ' ', ' ', ' ', ' ', ' ', '@'},
//                {'@', '@', '@', '@', '@', '@', '@'},
//                {'@', ' ', ' ', ' ', ' ', ' ', '@'}
//        }, "@", 3, 7, 11);
//        parcelRepository.setParcels(Arrays.asList(parcel));
//
//        Parcel updatedParcel = parcelRepository.updateName("Штанга", "Полка");
//
//        assertEquals("Полка", updatedParcel.getName());
//    }
}
