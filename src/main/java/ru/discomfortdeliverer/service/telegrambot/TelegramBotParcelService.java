package ru.discomfortdeliverer.service.telegrambot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.service.parcel.ParcelService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramBotParcelService {

    private static final String startMessage = "Привет! Чтобы посмотреть все поддерживаемые команды введи /help";
    private static final String helpMessage = "Команды для работы с посылками:\n\n" +
            "• /getall - Получить список всех посылок\n" +
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

    public String start() {
        return startMessage;
    }

    public String help() {
        return helpMessage;
    }

    public String getAll() {
        List<Parcel> allParcels = parcelService.getAll();
        return allParcels.toString();
    }

    public String getByName(String name) {
        Parcel parcel = parcelService.getByName(name);
        return parcel.toString();
    }

    public String deleteByName(String name) {
        Parcel deletedparcel = parcelService.deleteByName(name);
        return "Посылка удалена:\n" + deletedparcel.toString();
    }

    public String updateName(String oldName, String newName) {
        Parcel parcelWithUpdatedName = parcelService.updateName(oldName, newName);
        return "Имя посылки обновлено:\n" + parcelWithUpdatedName.toString();
    }

    public String updateSymbol(String parcelName, String newSymbol) {
        Parcel parcelWithUpdatedSymbol = parcelService.updateSymbol(parcelName, newSymbol);
        return "Символ посылки обновлен:\n" + parcelWithUpdatedSymbol.toString();
    }

    public String updateForm(String parcelName, String newForm, String symbol) {
        Parcel parcelWithUpdatedForm = parcelService.changeParcelFormFromRest(parcelName,
                newForm,
                symbol);
        return "Форма посылки обновлена:\n" + parcelWithUpdatedForm.toString();
    }
}
