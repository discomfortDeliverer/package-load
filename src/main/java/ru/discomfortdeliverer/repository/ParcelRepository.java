package ru.discomfortdeliverer.repository;

import jakarta.annotation.PostConstruct;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.discomfortdeliverer.exception.ParcelNotFoundException;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.service.parcel.FileParcelLoadService;
import ru.discomfortdeliverer.service.parcel.FileParcelSaveToFileService;

import java.util.ArrayList;
import java.util.List;

@Component
public class ParcelRepository {
    @Setter
    private List<Parcel> parcels;
    private final FileParcelLoadService fileParcelLoadService;
    private final FileParcelSaveToFileService fileParcelSaveToFileService;

    @Setter
    @Value("${repository.file.path}")
    private String pathToRepositoryFile;

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

    /**
     * Метод находит посылку по имени
     * @param parcelName Имя посылки, которую надо найти
     * @throws ParcelNotFoundException если посылка не найдена
     * @return Найденную по имени посылку
     */
    public Parcel findParcelByName(String parcelName) {
        for (Parcel parcel : parcels) {
            if (parcel.getName().equals(parcelName)) {
                return parcel;
            }
        }
        throw new ParcelNotFoundException("Посылка с именем " + parcelName + " не найдена");
    }

    /**
     * Метод возвращает список всех посылок в репозитории
     * @return Список всех существующих посылок
     */
    public List<Parcel> getAllParcels() {
        return parcels;
    }

    /**
     * Метод изменяет символ посылки
     * @param parcelName Имя посылки, символ которой нужно изменить
     * @param newSymbol Новый символ
     * @return Посылку с обновленным символом
     */
    public Parcel changeSymbol(String parcelName, String newSymbol) {
        Parcel parcel = findParcelByName(parcelName);
        parcel.changeSymbolTo(newSymbol);
        fileParcelSaveToFileService.save(pathToRepositoryFile, parcels);
        return parcel;
    }

    /**
     * Метод меняет форму мосылки
     * @param parcelName Имя посылки, форму которой нужно изменить
     * @param newForm Новая форма посылки
     * @param symbol Символ новой формы послыки
     * @return Посылку с обновленной формой и символом
     */
    public Parcel changeParcelForm(String parcelName, char[][] newForm, String symbol) {
        Parcel parcel = findParcelByName(parcelName);
        parcel.changeFormTo(newForm);
        parcel.setSymbol(symbol);
        fileParcelSaveToFileService.save(pathToRepositoryFile, parcels);
        return parcel;
    }

    /**
     * Метод удаляет посылку из репозитория по имени
     * @param parcelName Имя посылки, которую надо удалить
     * @throws ParcelNotFoundException если посылка с именем не найдена
     * @return Удаленная посылка
     */
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

    /**
     * Метод меняет имя посылки
     * @param oldName Имя посылки, имя которой надо изменить
     * @param newName Новое имя посылки
     * @return Посылку с обновленным именем
     */
    public Parcel changeParcelName(String oldName, String newName) {
        Parcel parcel = findParcelByName(oldName);
        parcel.setName(newName);
        fileParcelSaveToFileService.save(pathToRepositoryFile, parcels);
        return parcel;
    }
}
