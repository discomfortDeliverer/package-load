package ru.discomfortdeliverer.service.telegrambot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.discomfortdeliverer.entity.ParcelEntity;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.service.parcel.ParcelService;

import java.util.List;

@Slf4j
@Service
public class TelegramBotParcelService {

    private final String startMessage = "Привет! Чтобы посмотреть все поддерживаемые команды введи /help";
    private final String helpMessage = "Команды для работы с посылками:\n\n" +
            "• /getall - Получить список всех посылок\n" +
            "• /add - Добавить посылку. \nПример: /add Штанга $$$\\n$$$ $" +
            "• /getbyname - Получить посылку по имени. \nПример: /getbyname Штанга\n" +
            "• /deletebyname - Удалить посылку по имени. \nПример: /deletebyname Штанга\n" +
            "• /updatename - Изменить имя посылки. \nПример: /updatename СтароеИмя НовоеИмя\n" +
            "• /updatesymbol - Изменить символ посылки. \nПример: /updatesymbol ИмяПосылки $\n" +
            "• /updateform - Изменить форму посылки. \nПример: /updateform Имя посылки @@@\\n@@@\\n@@@ @\n\n" +
            "Команды для работы с погрузкой посылок:\n\n" +
            "• /simpleload - Загрузка посылок по имени простым методом. \nПример: /simpleload Штанга Стол Стул\n" +
            "• /optimalload - Загрузка посылок по имени, размеру грузовика и количеству грузовиков оптимальным " +
            "методом. \nПример: /optimalload Штанга Стол Стул 7x8 5\n" +
            "• /uniformload - Загрузка посылок по имени, размеру грузовика и количеству грузовиков равномерным " +
            "методом. \nПример: /uniformload Штанга Стол Стул 7x8 5\n" +
            "• Чтобы посмотреть какие посылки в загруженных грузовиках прикрепите файл txt или json";

    private final ParcelService parcelService;

    @Autowired
    public TelegramBotParcelService(ParcelService parcelService) {
        this.parcelService = parcelService;
    }

    public String start() {
        return startMessage;
    }

    public String help() {
        return helpMessage;
    }

    public String getAll() {
        List<Parcel> allParcels = parcelService.getAll();
        log.info("Получены все посылки - {}", allParcels.toString());
        return allParcels.toString();
    }

    public String getByName(String name) {
        log.info("Входной параметр name={}", name);
        Parcel parcel = parcelService.getByName(name);
        log.info("Получена посылка - {}", parcel);
        return parcel.toString();
    }

    public String deleteByName(String name) {
        log.info("Входной параметр name - {}", name);
        Parcel deletedparcel = parcelService.deleteByName(name);
        log.info("Посылка удалена - {}", deletedparcel);
        return "Посылка удалена:\n" + deletedparcel.toString();
    }

    public String updateName(String oldName, String newName) {
        log.info("Входные параметры oldName={}, newName={}", oldName, newName);
        Parcel parcelWithUpdatedName = parcelService.updateName(oldName, newName);
        log.info("Посылка с измененным именем - {}", parcelWithUpdatedName);
        return "Имя посылки обновлено:\n" + parcelWithUpdatedName.toString();
    }

    public String updateSymbol(String parcelName, String newSymbol) {
        log.info("Входные параметры parcelName={}, newSymbol={}", parcelName, newSymbol);
        Parcel parcelWithUpdatedSymbol = parcelService.updateSymbol(parcelName, newSymbol);
        log.info("Посылка с измененным символом и формой - {}", parcelWithUpdatedSymbol);
        return "Символ посылки обновлен:\n" + parcelWithUpdatedSymbol.toString();
    }

    public String updateForm(String parcelName, String newForm, String symbol) {
        log.info("Входные параметры parcelName={}, newForm={}, symbol={}", parcelName, newForm, symbol);
        Parcel parcelWithUpdatedForm = parcelService.changeParcelFormFromRest(parcelName,
                newForm,
                symbol);
        log.info("Посылка с измененным символом и формой - {}", parcelWithUpdatedForm);
        return "Форма посылки обновлена:\n" + parcelWithUpdatedForm.toString();
    }

    public String add(String name, String form, String symbol) {
        ParcelEntity parcelEntity = parcelService.addNewParcel(name, form, symbol);
        return parcelEntity.toString();
    }
}
