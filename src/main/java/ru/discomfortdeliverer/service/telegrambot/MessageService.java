package ru.discomfortdeliverer.service.telegrambot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.service.parcel.ParcelService;

import java.util.List;

@Service
public class MessageService {
    private final ParcelService parcelService;

    @Autowired
    public MessageService(ParcelService parcelService) {
        this.parcelService = parcelService;
    }

    public SendMessage messageReceiver(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String firstName = update.getMessage().getChat().getFirstName();

            String[] commandAndParameters = text.split(" ");
            String command = commandAndParameters[0];
            String responseText;
            switch (command) {
                case "/start":
                    responseText = "Привет";
                    break;
                case "/help":
                    responseText = getListSupportedCommands();
                    break;
                case "/getallparcels":
                    List<Parcel> allParcels = parcelService.getAllParcels();
                    responseText = allParcels.toString();
                    break;
                case "/getparcelbyname":
                    Parcel parcel = parcelService.getParcelByName(commandAndParameters[1]);
                    responseText = parcel.toString();
                    break;
                case "/deleteparcelbyname":
                    Parcel deletedparcel = parcelService.deleteParcelByName(commandAndParameters[1]);
                    responseText = "Посылка удалена:\n" + deletedparcel.toString();
                    break;
                case "/changeparcelname":
                    Parcel parcelWithUpdatedName = parcelService.changeParcelName(commandAndParameters[1], commandAndParameters[2]);
                    responseText = "Посылка обновлена:\n" + parcelWithUpdatedName.toString();
                    break;
                case "/changesymbol":
                    Parcel parcelWithUpdatedSymbol = parcelService.changeSymbol(commandAndParameters[1], commandAndParameters[2]);
                    responseText = "Посылка обновлена:\n" + parcelWithUpdatedSymbol.toString();
                    break;
                case "/changeparcelform":
                    Parcel parcelWithUpdatedForm = parcelService.changeParcelFormFromRest(commandAndParameters[1],
                            commandAndParameters[2],
                            commandAndParameters[3]);
                    responseText = "Посылка обновлена:\n" + parcelWithUpdatedForm.toString();
                    break;
                default: responseText = "Команда не поддерживается";
            }

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(responseText);
            return message;
        }
        return null;
    }

    private String getListSupportedCommands() {
        List<String> supportedCommands = List.of("/getallparcels - Получить список всех посылок",
                "/getparcelbyname - Получить посылку по имени. Пример: /getparcelbyname Штанга",
                "/deleteparcelbyname - Удалить посылку по имени. Пример: /deleteparcelbyname Штанга",
                "/changeparcelname - Изменить имя посылки. Пример: /changeparcelname СтароеИмя НовоеИмя",
                "/changesymbol - Изменить символ посылки. Пример: /changesymbol ИмяПосылки $",
                "/changeparcelform - Изменить форму посылки. Пример: /changeparcelform Имя посылки @@@\\n@@@\\n@@@ @");
        StringBuilder stringBuilder = new StringBuilder();
        for (String command : supportedCommands) {
            stringBuilder.append(command);
            stringBuilder.append(System.lineSeparator());
        }

        return stringBuilder.toString();
    }
}
