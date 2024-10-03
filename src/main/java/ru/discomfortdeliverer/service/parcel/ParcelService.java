package ru.discomfortdeliverer.service.parcel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.repository.ParcelRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ParcelService {
    private final ParcelRepository parcelRepository;

    @Autowired
    public ParcelService(ParcelRepository parcelRepository) {
        this.parcelRepository = parcelRepository;
    }

    /**
     * Метод возвращает из репозитория все посылки
     * @return Список со всеми поылками
     */
    public List<Parcel> getAllParcels() {
        log.info("Вызван метод getAllParcels()");
        return parcelRepository.getAllParcels();
    }

    /**
     * Метод изменяет символ в посылки в репозитории
     * @param parcelName Имя посылки, символ которой надо изменить
     * @param newSymbol Новый символ
     * @return Посылку с обновленным символом
     */
    public Parcel changeSymbol(String parcelName, String newSymbol) {
        log.info("Вызван метод changeSymbol, parcelName={}, newSymbol={}", parcelName, newSymbol);
        return parcelRepository.changeSymbol(parcelName, newSymbol);
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
        char[][] charNewForm = convertStringFormIntoCharArrayForm(newForm);
        reverseForm(charNewForm);
        return parcelRepository.changeParcelForm(parcelName, charNewForm, symbol);
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
    public Parcel showParcelByName(String parcelName) {
        log.info("Вызван метод showParcelByName, parcelName={}", parcelName);
        return parcelRepository.findParcelByName(parcelName);
    }

    /**
     * Метод удаляет посылку из репозитория по ее имени
     * @param parcelName Имя посылки, которую надо удалить
     * @return Удаленную посылку
     */
    public Parcel deleteParcelByName(String parcelName) {
        log.info("Вызван метод deleteParcelByName, parcelName={}", parcelName);
        return parcelRepository.deleteParcelByName(parcelName);
    }

    /**
     * Метод изменяет имя посылки в репозитории
     * @param oldName Имя посылки, которое надо изменить
     * @param newName Новое имя
     * @return Посылку с обновленным именем
     */
    public Parcel changeParcelName(String oldName, String newName) {
        log.info("Вызван метод changeParcelName, oldName={}, newName={}", oldName, newName);
        return parcelRepository.changeParcelName(oldName, newName);
    }

    /**
     * Метод находит посылки по их именам, разделенных зарятой
     * @param parcelNames Имена посылок, которые надо найти
     * @return Список с найденными по имени посылками
     */
    public List<Parcel> findParcelsByNames(String parcelNames) {
        log.info("Вызван метод findParcelsByNames, parcelNames={}", parcelNames);
        String[] names = parcelNames.split(",");

        List<Parcel> parcels = new ArrayList<>();
        for (String name : names) {
            Parcel parcel = showParcelByName(name);
            parcels.add(parcel);
        }
        log.debug("Найденные посылки по именам={}, parcels={}", parcelNames, parcels);
        return parcels;
    }
}
