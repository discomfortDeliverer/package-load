package ru.discomfortdeliverer.repository;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.discomfortdeliverer.exception.ParcelNotFoundException;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.service.parcel.FileParcelLoadService;
import ru.discomfortdeliverer.service.parcel.FileParcelSaveToFileService;

import java.util.ArrayList;
import java.util.List;

@Component
public class ParcelRepository {
    private List<Parcel> parcels;
    private final FileParcelLoadService fileParcelLoadService;
    private final FileParcelSaveToFileService fileParcelSaveToFileService;
    private final String pathToRepositoryFile = "src/main/resources/repository/parcels.txt";

    @Autowired
    public ParcelRepository(FileParcelLoadService fileParcelLoadService,
                            FileParcelSaveToFileService fileParcelSaveToFileService) {
        this.parcels = new ArrayList<>();
        this.fileParcelLoadService = fileParcelLoadService;
        this.fileParcelSaveToFileService = fileParcelSaveToFileService;
    }

    @PostConstruct
    public void init() {
        parcels = fileParcelLoadService.loadParcelsFromFile(pathToRepositoryFile);
    }

    public Parcel findParcelByName(String parcelName) {
        for (Parcel parcel : parcels) {
            if (parcel.getName().equals(parcelName)) {
                return parcel;
            }
        }
        throw new ParcelNotFoundException("Посылка с именем " + parcelName + " не найдена");
    }

    public List<Parcel> getAllParcels() {
        return parcels;
    }

    public Parcel changeSymbol(String parcelName, String newSymbol) {
        Parcel parcel = findParcelByName(parcelName);
        parcel.changeSymbolTo(newSymbol);
        fileParcelSaveToFileService.save(pathToRepositoryFile, parcels);
        return parcel;
    }

    public Parcel changeParcelForm(String parcelName, char[][] newForm, String symbol) {
        Parcel parcel = findParcelByName(parcelName);
        parcel.changeFormTo(newForm);
        parcel.setSymbol(symbol);
        fileParcelSaveToFileService.save(pathToRepositoryFile, parcels);
        return parcel;
    }

    public Parcel deleteParcelByName(String parcelName) {
        for (Parcel parcel : parcels) {
            if (parcel.getName().equals(parcelName)) {
                parcels.remove(parcel);
                fileParcelSaveToFileService.save(pathToRepositoryFile, parcels);
                return parcel;
            }
        }
        throw new ParcelNotFoundException("Посылка с именем " + parcelName + " не найдена");
    }

    public Parcel changeParcelName(String oldName, String newName) {
        Parcel parcel = findParcelByName(oldName);
        parcel.setName(newName);
        fileParcelSaveToFileService.save(pathToRepositoryFile, parcels);
        return parcel;
    }
}
