package ru.discomfortdeliverer.controller.shellcontrollers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.discomfortdeliverer.entity.ParcelEntity;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.service.parcel.ParcelService;

import java.util.List;

@ShellComponent
@Slf4j
public class ParcelShellController {
    private final ParcelService parcelService;

    @Autowired
    public ParcelShellController(ParcelService parcelService) {
        this.parcelService = parcelService;
    }

    /**
     * Метод выводит в консоль все посылки из памяти приложения
     * @return Список всех посылок
     */
    @ShellMethod(key = "get-all-parcels", value = "Показывает все посылки")
    public List<Parcel> getAllParcels() {
        log.info("Вызван метод getAllParcels");
        return parcelService.getAllParcels();
    }

    /**
     * Метод возвращает посылку по имени из памяти приложения
     * @param parcelName Имя посылки с большой буквы
     * @return Найденную по имени посылку
     */
    @ShellMethod(key = "show-parcel-by-name", value = "Показывает посылку по имени")
    public Parcel showParcelByName(String parcelName) {
        log.info("Вызван метод getParcelByName, parcelName={}", parcelName);
        return parcelService.getParcelByName(parcelName);
    }

    /**
     * Метод удаляет посылку по имени
     * @param parcelName Имя посылки, которую надо удалить с большой буквы
     * @return Удаленную посылку
     */
    @ShellMethod(key = "delete-parcel-by-name", value = "Удаляет посылку по имени")
    public String deleteParcelByName(String parcelName) {
        log.info("Вызван метод deleteParcelByName, parcelName={}", parcelName);
        return "Удалена посылка - " + parcelService.deleteParcelByName(parcelName);
    }

    @ShellMethod(key = "add", value = "Добавляет посылку")
    public ParcelEntity addNewParcel(String parcelName, String parcelForm, String parcelSymbol) {
        log.info("Вызван метод deleteParcelByName, parcelName={}", parcelName);
        return parcelService.addNewParcel(parcelName, parcelForm, parcelSymbol);
    }

    /**
     * Метод меняет имя посылки
     * @param oldName Имя посылки, которое надо изменить
     * @param newName Новое имя посылки
     * @return Посылку с обновленным именем
     */
    @ShellMethod(key = "change-parcel-name", value = "Изменить имя посылки")
    public String changeParcelName(String oldName, String newName) {
        log.info("Вызван метод changeParcelName, oldName={}, newName={}", oldName, newName);
        Parcel parcel = parcelService.changeParcelName(oldName, newName);
        if (parcel == null) {
            return "Не удалось обновить имя посылки";
        }
        return "Имя посылки успешно обновлено " + parcel;
    }

    /**
     * Метод меняет символ, которым рисуется посылка
     * @param parcelName Имя посылки, в которой надо изменить символ
     * @param newSymbol Новый символ отрисовки посылки
     * @return Посылку с обновленным символом
     */
    @ShellMethod(key = "change-symbol", value = "Меняет символ, которым описывается посылка")
    public Parcel changeSymbol(String parcelName, String newSymbol) {
        log.info("Вызван метод changeSymbol, parcelName={}, newSymbol={}", parcelName, newSymbol);
        return parcelService.changeSymbol(parcelName, newSymbol);
    }

    /**
     * Метод меняет форму посылки, новую форму надо указывать через \n
     * @param parcelName Имя посылки, форму которой мы будем менять
     * @param newForm Новая форма посылки, указать строки разделенные \n
     * @param symbol Новый символ посылки
     * @return Возвращает посылку с обновленной формой
     */
    @ShellMethod(key = "change-parcel-form", value = "Меняет форму посылки, разделение строки указывать через \\n")
    public String changeParcelForm(String parcelName, String newForm, String symbol) {
        log.info("Вызван метод changeParcelForm, parcelName={}, newForm={}, symbol={}", parcelName, newForm, symbol);
        Parcel parcel = parcelService.changeParcelForm(parcelName, newForm, symbol);
        if (parcel == null) {
            return "Ошибка изменения формы, неверная форма - " + newForm;
        }
        return "Форма успешно изменена - " + parcel;
    }
}
