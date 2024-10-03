package ru.discomfortdeliverer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.service.parcel.ParcelService;

import java.util.List;

@ShellComponent
public class ParcelController {
    private final ParcelService parcelService;

    @Autowired
    public ParcelController(ParcelService parcelService) {
        this.parcelService = parcelService;
    }

    /**
     * Метод выводит в консоль все посылки из памяти приложения
     * @return Список всех посылок
     */
    @ShellMethod(key = "get-all-parcels", value = "Показывает все посылки")
    public List<Parcel> getAllParcels() {
        return parcelService.getAllParcels();
    }

    /**
     * Метод возвращает посылку по имени из памяти приложения
     * @param parcelName Имя посылки с большой буквы
     * @return Найденную по имени посылку
     */
    @ShellMethod(key = "show-parcel-by-name", value = "Показывает посылку по имени")
    public Parcel showParcelByName(String parcelName) {
        return parcelService.showParcelByName(parcelName);
    }

    /**
     * Метод удаляет посылку по имени
     * @param parcelName Имя посылки, которую надо удалить с большой буквы
     * @return Удаленную посылку
     */
    @ShellMethod(key = "delete-parcel-by-name", value = "Удаляет посылку по имени")
    public Parcel deleteParcelByName(String parcelName) {
        return parcelService.deleteParcelByName(parcelName);
    }

    /**
     * Метод меняет имя посылки
     * @param oldName Имя посылки, которое надо изменить
     * @param newName Новое имя посылки
     * @return Посылку с обновленным именем
     */
    @ShellMethod(key = "change-parcel-name", value = "Изменить имя посылки")
    public Parcel changeParcelName(String oldName, String newName) {
        return parcelService.changeParcelName(oldName, newName);
    }

    /**
     * Метод меняет символ, которым рисуется посылка
     * @param parcelName Имя посылки, в которой надо изменить символ
     * @param newSymbol Новый символ отрисовки посылки
     * @return Посылку с обновленным символом
     */
    @ShellMethod(key = "change-symbol", value = "Меняет символ, которым описывается посылка")
    public Parcel changeSymbol(String parcelName, String newSymbol) {
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
    public Parcel changeParcelForm(String parcelName, String newForm, String symbol) {
        return parcelService.changeParcelForm(parcelName, newForm, symbol);
    }
}
