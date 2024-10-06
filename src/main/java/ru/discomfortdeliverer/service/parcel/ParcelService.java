package ru.discomfortdeliverer.service.parcel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.discomfortdeliverer.entity.ParcelEntity;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.repository.ParcelRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ParcelService {
    private final ParcelRepository parcelRepository;
    private final ParcelEntityToParcelMapper parcelEntityToParcelMapper;
    private final ParcelToParcelEntityMapper parcelToParcelEntityMapper;

    @Autowired
    public ParcelService(ParcelRepository parcelRepository,
                         ParcelEntityToParcelMapper parcelEntityToParcelMapper,
                         ParcelToParcelEntityMapper parcelToParcelEntityMapper) {
        this.parcelRepository = parcelRepository;
        this.parcelEntityToParcelMapper = parcelEntityToParcelMapper;
        this.parcelToParcelEntityMapper = parcelToParcelEntityMapper;
    }

    /**
     * Метод возвращает из репозитория все посылки
     * @return Список со всеми поылками
     */
    public List<Parcel> getAllParcels() {
        log.info("Вызван метод getAllParcels()");
        List<ParcelEntity> allParcelEntities = parcelRepository.findAll();

        return parcelEntityToParcelMapper.mapParcelEntityListToParcelList(allParcelEntities);
    }

    /**
     * Метод изменяет символ в посылки в репозитории
     * @param parcelName Имя посылки, символ которой надо изменить
     * @param newSymbol Новый символ
     * @return Посылку с обновленным символом
     */
    public Parcel changeSymbol(String parcelName, String newSymbol) {
        log.info("Вызван метод changeSymbol, parcelName={}, newSymbol={}", parcelName, newSymbol);
        ParcelEntity foundParcelEntity = parcelRepository.findByName(parcelName);
        Parcel parcel = parcelEntityToParcelMapper.mapParcelEntityToParcel(foundParcelEntity);

        parcel.changeSymbolTo(newSymbol);

        ParcelEntity parcelEntity = parcelToParcelEntityMapper.mapParcelToParcelEntity(parcel);
        parcelRepository.updateParcelSymbolByName(parcelName, parcelEntity.getForm(), newSymbol);

        return parcel;
    }

    /**
     * Метод меняет форму посылки в репозитории
     * @param parcelName Имя посылки, форму которой надо изменить
     * @param newForm Новая форма посылки в виде строки
     * @param symbol Новый символ посылки
     * @return Посылку с обновленной формой
     */
    public Parcel changeParcelForm(String parcelName, String newForm, String symbol) {
        log.info("Вызван метод changeParcelForm, parcelName={}, newForm={}, symbol={}", parcelName, newForm, symbol);
        newForm = newForm.replace("n", System.lineSeparator());
        int parcel = parcelRepository.updateParcelByName(parcelName, newForm, symbol);
        if (parcel > 0) {
            ParcelEntity foundParcelEntity = parcelRepository.findByName(parcelName);
            return parcelEntityToParcelMapper.mapParcelEntityToParcel(foundParcelEntity);
        }
        return null;
    }

    public Parcel changeParcelFormFromRest(String parcelName, String newForm, String symbol) {
        log.info("Вызван метод changeParcelFormFromRest, parcelName={}, newForm={}, symbol={}", parcelName, newForm, symbol);
        newForm = newForm.replace("\n", "n");
        return changeParcelForm(parcelName, newForm, symbol);
    }

    private char[][] convertStringFormIntoCharArrayForm(String newForm) {
        log.info("Вызван метод convertStringFormIntoCharArrayForm,newForm={}", newForm);
        String[] split = newForm.split("n");
        int maxLength = 0;
        for (String line : split) {
            if (line.length() > maxLength) {
                maxLength = line.length();
            }
        }

        char[][] form = new char[split.length][maxLength];
        for (char[] chars : form) {
            Arrays.fill(chars, ' ');
        }

        for (int i = 0; i < form.length; i++) {
            String line = split[i];
            for (int j = 0; j < line.length(); j++) {
                form[i][j] = line.charAt(j);
            }
        }
        log.debug("В методе convertStringFormIntoCharArrayForm получена форма - form={}", form);
        return form;
    }

    private void reverseForm(char[][] form) {
        log.debug("Вызван метод reverseForm, form={}", form);
        for (int i = 0; i < form.length / 2; i++) {
            // Меняем местами строки
            char[] temp = form[i];
            form[i] = form[form.length - 1 - i];
            form[form.length - 1 - i] = temp;
        }
        log.debug("Измененная форма в методе reverseForm, form={}", form);
    }

    /**
     * Метод находит посылку по имени в репозитории
     * @param parcelName Имя посылки, которую надо найти
     * @return Посылку, найденную по имени
     */
    public Parcel getParcelByName(String parcelName) {
        log.info("Вызван метод getParcelByName, parcelName={}", parcelName);
        ParcelEntity foundParcelEntity = parcelRepository.findByName(parcelName);
        return parcelEntityToParcelMapper.mapParcelEntityToParcel(foundParcelEntity);
    }

    /**
     * Метод удаляет посылку из репозитория по ее имени
     * @param parcelName Имя посылки, которую надо удалить
     * @return Удаленную посылку
     */
    public Parcel deleteParcelByName(String parcelName) {
        log.info("Вызван метод deleteParcelByName, parcelName={}", parcelName);
        ParcelEntity foundParcelEntity = parcelRepository.findByName(parcelName);
        parcelRepository.delete(foundParcelEntity);
        return parcelEntityToParcelMapper.mapParcelEntityToParcel(foundParcelEntity);
    }

    /**
     * Метод изменяет имя посылки в репозитории
     * @param oldName Имя посылки, которое надо изменить
     * @param newName Новое имя
     * @return Посылку с обновленным именем
     */
    public Parcel changeParcelName(String oldName, String newName) {
        log.info("Вызван метод changeParcelName, oldName={}, newName={}", oldName, newName);
        int updatedLines = parcelRepository.updateParcelNameByName(oldName, newName);
        if (updatedLines > 0) {
            ParcelEntity updatedParcelEntity = parcelRepository.findByName(newName);
            return parcelEntityToParcelMapper.mapParcelEntityToParcel(updatedParcelEntity);
        }
        return null;
    }

    /**
     * Метод находит посылки по их именам, разделенных зарятой
     * @param parcelNames Имена посылок, которые надо найти
     * @return Список с найденными по имени посылками
     */
    public List<Parcel> findParcelsByNames(List<String> parcelNames) {
        log.info("Вызван метод findParcelsByNames, parcelNames={}", parcelNames);

        List<Parcel> parcels = new ArrayList<>();
        for (String name : parcelNames) {
            Parcel parcel = getParcelByName(name);
            parcels.add(parcel);
        }
        log.debug("Найденные посылки по именам={}, parcels={}", parcelNames, parcels);
        return parcels;
    }

    public ParcelEntity addNewParcel(String parcelName, String parcelForm, String parcelSymbol) {
        parcelForm = parcelForm.replace("n", System.lineSeparator());
        ParcelEntity parcel = new ParcelEntity();
        parcel.setName(parcelName);
        parcel.setForm(parcelForm);
        parcel.setSymbol(parcelSymbol);
        ParcelEntity save = parcelRepository.save(parcel);
        return save;
    }
}
