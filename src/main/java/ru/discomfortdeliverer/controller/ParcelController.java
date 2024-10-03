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

    @ShellMethod(key = "get-all-parcels", value = "Показывает все посылки")
    public List<Parcel> getAllParcels() {
        return parcelService.getAllParcels();
    }

    @ShellMethod(key = "show-parcel-by-name", value = "Показывает посылку по имени")
    public Parcel showParcelByName(String parcelName) {
        return parcelService.showParcelByName(parcelName);
    }

    @ShellMethod(key = "delete-parcel-by-name", value = "Удаляет посылку по имени")
    public Parcel deleteParcelByName(String parcelName) {
        return parcelService.deleteParcelByName(parcelName);
    }

    @ShellMethod(key = "change-parcel-name", value = "Изменить имя посылки")
    public Parcel changeParcelName(String oldName, String newName) {
        return parcelService.changeParcelName(oldName, newName);
    }

    @ShellMethod(key = "change-symbol", value = "Меняет символ, которым описывается посылка")
    public Parcel changeSymbol(String parcelName, String newSymbol) {
        return parcelService.changeSymbol(parcelName, newSymbol);
    }

    @ShellMethod(key = "change-parcel-form", value = "Меняет форму посылки, разделение строки указывать через \\n")
    public Parcel changeParcelForm(String parcelName, String newForm, String symbol) {
        return parcelService.changeParcelForm(parcelName, newForm, symbol);
    }
}
