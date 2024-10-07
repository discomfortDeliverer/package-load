package ru.discomfortdeliverer.controller.shellcontrollers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.discomfortdeliverer.entity.ParcelEntity;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.service.parcel.ParcelService;

import java.util.List;


@Slf4j
@ShellComponent
@RequiredArgsConstructor
public class ParcelShellController {
    private final ParcelService parcelService;

    /**
     * Метод выводит в консоль все посылки из памяти приложения
     * @return Список всех посылок
     */
    @ShellMethod(key = "get-all", value = "Показывает все посылки")
    public List<Parcel> getAll() {
        log.info("Вызван метод getAll");
        return parcelService.getAll();
    }

    /**
     * Метод возвращает посылку по имени из памяти приложения
     * @param parcelName Имя посылки с большой буквы
     * @return Найденную по имени посылку
     */
    @ShellMethod(key = "get-by-name", value = "Показывает посылку по имени")
    public Parcel getByName(String parcelName) {
        log.info("Вызван метод getByName, parcelName={}", parcelName);
        return parcelService.getByName(parcelName);
    }

    /**
     * Метод удаляет посылку по имени
     * @param parcelName Имя посылки, которую надо удалить с большой буквы
     * @return Удаленную посылку
     */
    @ShellMethod(key = "delete-by-name", value = "Удаляет посылку по имени")
    public String deleteByName(String parcelName) {
        log.info("Вызван метод deleteByName, parcelName={}", parcelName);
        return "Удалена посылка - " + parcelService.deleteByName(parcelName);
    }

    @ShellMethod(key = "add", value = "Добавляет посылку")
    public ParcelEntity addNewParcel(String parcelName, String parcelForm, String parcelSymbol) {
        log.info("Вызван метод deleteByName, parcelName={}", parcelName);
        return parcelService.addNewParcel(parcelName, parcelForm, parcelSymbol);
    }

    /**
     * Метод меняет имя посылки
     * @param oldName Имя посылки, которое надо изменить
     * @param newName Новое имя посылки
     * @return Посылку с обновленным именем
     */
    @ShellMethod(key = "update-name", value = "Изменить имя посылки")
    public String updateName(String oldName, String newName) {
        log.info("Вызван метод updateName, oldName={}, newName={}", oldName, newName);
        Parcel parcel = parcelService.updateName(oldName, newName);
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
    @ShellMethod(key = "update-symbol", value = "Меняет символ, которым описывается посылка")
    public Parcel updateSymbol(String parcelName, String newSymbol) {
        log.info("Вызван метод updateSymbol, parcelName={}, newSymbol={}", parcelName, newSymbol);
        return parcelService.updateSymbol(parcelName, newSymbol);
    }

    /**
     * Метод меняет форму посылки, новую форму надо указывать через \n
     * @param parcelName Имя посылки, форму которой мы будем менять
     * @param newForm Новая форма посылки, указать строки разделенные \n
     * @param symbol Новый символ посылки
     * @return Возвращает посылку с обновленной формой
     */
    @ShellMethod(key = "update-form", value = "Меняет форму посылки, разделение строки указывать через \\n")
    public String updateForm(String parcelName, String newForm, String symbol) {
        log.info("Вызван метод updateForm, parcelName={}, newForm={}, symbol={}", parcelName, newForm, symbol);
        Parcel parcel = parcelService.updateForm(parcelName, newForm, symbol);
        if (parcel == null) {
            return "Ошибка изменения формы, неверная форма - " + newForm;
        }
        return "Форма успешно изменена - " + parcel;
    }
}
