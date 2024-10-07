package ru.discomfortdeliverer.service.telegrambot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.discomfortdeliverer.config.properties.BotProperties;
import ru.discomfortdeliverer.model.parcel.Parcel;
import ru.discomfortdeliverer.model.truck.Truck;
import ru.discomfortdeliverer.model.truck.TruckParcelsCounterWrapper;
import ru.discomfortdeliverer.service.parcel.FileParcelLoadService;
import ru.discomfortdeliverer.service.parcel.ParcelService;
import ru.discomfortdeliverer.service.truck.FileTruckLoadService;
import ru.discomfortdeliverer.service.truck.OptimalTruckLoader;
import ru.discomfortdeliverer.service.truck.ParcelCounterService;
import ru.discomfortdeliverer.service.truck.SimpleTruckLoader;
import ru.discomfortdeliverer.service.truck.UniformTruckLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final ParcelService parcelService;
    private final FileParcelLoadService fileParcelLoadService;
    private final SimpleTruckLoader simpleTruckLoader;
    private final OptimalTruckLoader optimalTruckLoader;
    private final UniformTruckLoader uniformTruckLoader;
    private final BotProperties botProperties;
    private final FileTruckLoadService fileTruckLoadService;
    private final ParcelCounterService parcelCounterService;


    public SendMessage messageReceiver(Update update) {
        String responseText;
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String firstName = update.getMessage().getChat().getFirstName();

            String[] commandAndParameters = text.split(" ");
            String command = commandAndParameters[0];

            switch (command) {
                case "/start":
                    responseText = "Привет! Чтобы посмотреть все поддерживаемые команды введи /help";
                    break;
                case "/help":
                    responseText = getListSupportedCommands();
                    break;
                case "/getall":
                    List<Parcel> allParcels = parcelService.getAll();
                    responseText = allParcels.toString();
                    break;
                case "/getbyname":
                    Parcel parcel = parcelService.getByName(commandAndParameters[1]);
                    responseText = parcel.toString();
                    break;
                case "/deletebyname":
                    Parcel deletedparcel = parcelService.deleteByName(commandAndParameters[1]);
                    responseText = "Посылка удалена:\n" + deletedparcel.toString();
                    break;
                case "/updatename":
                    Parcel parcelWithUpdatedName = parcelService.updateName(commandAndParameters[1], commandAndParameters[2]);
                    responseText = "Посылка обновлена:\n" + parcelWithUpdatedName.toString();
                    break;
                case "/updatesymbol":
                    Parcel parcelWithUpdatedSymbol = parcelService.updateSymbol(commandAndParameters[1], commandAndParameters[2]);
                    responseText = "Посылка обновлена:\n" + parcelWithUpdatedSymbol.toString();
                    break;
                case "/updateform":
                    Parcel parcelWithUpdatedForm = parcelService.changeParcelFormFromRest(commandAndParameters[1],
                            commandAndParameters[2],
                            commandAndParameters[3]);
                    responseText = "Посылка обновлена:\n" + parcelWithUpdatedForm.toString();
                    break;
                case "/simpleload":
                    List<String> parcelNames = new ArrayList<>(Arrays.asList(commandAndParameters).subList(1, commandAndParameters.length));

                    List<Parcel> parcels = parcelService.findParcelsByNames(parcelNames);
                    List<Truck> trucks = simpleTruckLoader.loadParcels(parcels);

                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        String jsonResult = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(trucks);
                        responseText = "Результат погрузки простым методом:\n\n" + jsonResult;
                    } catch (JsonProcessingException e) {
                        responseText = "Произошла ошибка погрузки";
                        throw new RuntimeException(e);
                    }
                    break;
                case "/optimalload":
                    if (commandAndParameters.length < 4) {
                        responseText = "Некорректный ввод";
                        break;
                    }
                    parcelNames = new ArrayList<>(Arrays.asList(commandAndParameters)
                            .subList(1, commandAndParameters.length - 2));
                    String truckSize = commandAndParameters[commandAndParameters.length - 2];
                    String maxTrucks = commandAndParameters[commandAndParameters.length - 1];

                    parcels = parcelService.findParcelsByNames(parcelNames);
                    trucks = optimalTruckLoader.loadParcels(parcels, truckSize, maxTrucks);

                    objectMapper = new ObjectMapper();
                    try {
                        String jsonResult = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(trucks);
                        responseText = "Результат погрузки простым методом:\n\n" + jsonResult;
                    } catch (JsonProcessingException e) {
                        responseText = "Произошла ошибка погрузки";
                        throw new RuntimeException(e);
                    }
                    break;
                case "/uniformload":
                    if (commandAndParameters.length < 4) {
                        responseText = "Некорректный ввод";
                        break;
                    }
                    parcelNames = new ArrayList<>(Arrays.asList(commandAndParameters)
                            .subList(1, commandAndParameters.length - 2));
                    truckSize = commandAndParameters[commandAndParameters.length - 2];
                    maxTrucks = commandAndParameters[commandAndParameters.length - 1];

                    parcels = parcelService.findParcelsByNames(parcelNames);
                    trucks = uniformTruckLoader.loadParcels(parcels, truckSize, maxTrucks);

                    objectMapper = new ObjectMapper();
                    try {
                        String jsonResult = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(trucks);
                        responseText = "Результат погрузки простым методом:\n\n" + jsonResult;
                    } catch (JsonProcessingException e) {
                        responseText = "Произошла ошибка погрузки";
                        throw new RuntimeException(e);
                    }
                    break;
                default: responseText = "Команда не поддерживается";
            }

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(responseText);
            return message;
        }
        if (update.hasMessage() && update.getMessage().hasDocument()) {
            Long chatId = update.getMessage().getChatId();
            if (update.getMessage().hasDocument()) {
                Document document = update.getMessage().getDocument();

                if (document != null) {
                    String fileId = document.getFileId();
                    String fileName = document.getFileName();

                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        downloadFile(fileId, fileName);
                        List<Truck> trucks = fileTruckLoadService.loadTrucksFromJsonFile("src/main/resources/uploaded_files/" + fileName);
                        TruckParcelsCounterWrapper truckParcelsCounterWrapper = parcelCounterService.countEachTypeParcels(trucks);
                        String jsonResult = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(trucks);
                        responseText = jsonResult;
                    } catch (IOException e) {
                        responseText = "Ошибка загрузки";
                        throw new RuntimeException(e);
                    }
                } else {
                    responseText = "Прикрепите файл";
                }

            } else {
                responseText = "Прикрепите Json-файл с грузовиками";
            }
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(responseText);
            return message;
        }
        return null;
    }

    private void downloadFile(String fileId, String fileName) throws IOException {
        URL url = new URL("https://api.telegram.org/bot" + botProperties.token() + "/getFile?file_id=" + fileId);

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        String getFileResponse = br.readLine();

        JSONObject jsonResult = new JSONObject(getFileResponse);
        JSONObject path = jsonResult.getJSONObject("result");
        String filePath = path.getString("file_path");

        File localFile = new File("C:\\Users\\Maxim\\IdeaProjects\\Package_load\\src\\main\\resources\\uploaded_files\\" + fileName);
        InputStream inputStream = new URL("https://api.telegram.org/file/bot" + botProperties.token() +
                "/" + filePath).openStream();

        FileUtils.copyInputStreamToFile(inputStream, localFile);

        br.close();
        inputStream.close();

        System.out.println("Файл загружен");
    }
    private String getListSupportedCommands() {
        List<String> supportedCommands = List.of("Команды для работы с посылками:\n\n" +
                        "• /getall - Получить список всех посылок\n",
                "• /getbyname - Получить посылку по имени. \nПример: /getbyname Штанга\n",
                "• /deletebyname - Удалить посылку по имени. \nПример: /deletebyname Штанга\n",
                "• /updatename - Изменить имя посылки. \nПример: /updatename СтароеИмя НовоеИмя\n",
                "• /updatesymbol - Изменить символ посылки. \nПример: /updatesymbol ИмяПосылки $\n",
                "• /updateform - Изменить форму посылки. \nПример: /updateform Имя посылки @@@\\n@@@\\n@@@ @\n\n",
                "Команды для работы с погрузкой посылок:\n\n",
                "• /simpleload - Загрузка посылок по имени простым методом. \nПример: /simpleload Штанга Стол Стул\n",
                "• /optimalload - Загрузка посылок по имени, размеру грузовика и количеству грузовиков оптимальным " +
                        "методом. \nПример: /optimalload Штанга Стол Стул 7x8 5\n");
        StringBuilder stringBuilder = new StringBuilder();
        for (String command : supportedCommands) {
            stringBuilder.append(command);
            stringBuilder.append(System.lineSeparator());
        }

        return stringBuilder.toString();
    }
}
