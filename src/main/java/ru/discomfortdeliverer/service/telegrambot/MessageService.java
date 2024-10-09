package ru.discomfortdeliverer.service.telegrambot;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.discomfortdeliverer.config.properties.BotProperties;
import ru.discomfortdeliverer.model.truck.Truck;
import ru.discomfortdeliverer.model.truck.TruckParcelsCounterWrapper;
import ru.discomfortdeliverer.service.truck.FileTruckLoadService;
import ru.discomfortdeliverer.service.truck.ParcelCounterService;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final BotProperties botProperties;
    private final FileTruckLoadService fileTruckLoadService;
    private final ParcelCounterService parcelCounterService;
    private final TelegramBotParcelService telegramBotParcelService;
    private final TelegramBotTruckService telegramBotTruckService;

    public SendMessage messageReceiver(Update update) {
        String responseText = "";
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            log.debug("Получено сообщение - {}", text);
            String[] commandAndParameters = text.split(" ");
            String command = commandAndParameters[0];

            switch (command) {
                case "/start":
                    log.info("Выполняется метод /start");
                    responseText = telegramBotParcelService.start();
                    break;
                case "/help":
                    log.info("Выполняется метод /help");
                    responseText = telegramBotParcelService.help();
                    break;
                case "/getall":
                    log.info("Выполняется метод /getall");
                    responseText = telegramBotParcelService.getAll();
                    break;
                case "/getbyname":
                    log.info("Выполняется метод /getbyname");
                    responseText = telegramBotParcelService.getByName(commandAndParameters[1]);
                    break;
                case "/deletebyname":
                    log.info("Выполняется метод /deletebyname");
                    responseText = telegramBotParcelService.deleteByName(commandAndParameters[1]);
                    break;
                case "/updatename":
                    log.info("Выполняется метод /updatename");
                    responseText = telegramBotParcelService.updateName(commandAndParameters[1], commandAndParameters[2]);
                    break;
                case "/updatesymbol":
                    log.info("Выполняется метод /updatesymbol");
                    responseText = telegramBotParcelService.updateSymbol(commandAndParameters[1], commandAndParameters[2]);
                    break;
                case "/updateform":
                    log.info("Выполняется метод /updateform");
                    responseText = telegramBotParcelService.updateForm(commandAndParameters[1], commandAndParameters[2],
                            commandAndParameters[3]);
                    break;
                case "/simpleload":
                    log.info("Выполняется метод /simpleload");
                    responseText = telegramBotTruckService.simpleLoad(Arrays.copyOfRange(commandAndParameters, 1, commandAndParameters.length));
                    break;
                case "/optimalload":
                    log.info("Выполняется метод /optimalload");
                    if (commandAndParameters.length < 4) {
                        responseText = "Некорректный ввод";
                    } else {
                        String[] parcelNames = Arrays.copyOfRange(commandAndParameters, 1, commandAndParameters.length - 2);
                        String truckSize = commandAndParameters[commandAndParameters.length - 2];
                        String maxTrucks = commandAndParameters[commandAndParameters.length - 1];
                        responseText = telegramBotTruckService.optimalLoad(parcelNames, truckSize, maxTrucks);
                    }
                    break;
                case "/uniformload":
                    log.info("Выполняется метод /uniformload");
                    if (commandAndParameters.length < 4) {
                        log.error("Некорректное сообщение - {}", text);
                        responseText = "Некорректный ввод";
                    } else {
                        String[] parcelNames = Arrays.copyOfRange(commandAndParameters, 1, commandAndParameters.length - 2);
                        String truckSize = commandAndParameters[commandAndParameters.length - 2];
                        String maxTrucks = commandAndParameters[commandAndParameters.length - 1];
                        responseText = telegramBotTruckService.uniformLoad(parcelNames, truckSize, maxTrucks);
                    }
                    break;
                default:
                    log.error("Команда не поддерживается - {}", command);
                    responseText = "Команда не поддерживается";
            }

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(responseText);
            log.info("Отправлено сообщение в чат - {}", responseText);
            return message;
        }
        if (update.hasMessage() && update.getMessage().hasDocument()) {
            log.info("Пользователь прислал файл");
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
                        responseText = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(truckParcelsCounterWrapper);
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
            log.info("Отправлено сообщение в чат - {}", responseText);
            return message;
        }
        return null;
    }

    private void downloadFile(String fileId, String fileName) throws IOException {
        log.info("Попытка загрузить файл - {}", fileName);
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

        log.info("Файл - {} загружен и доступен по пути - {}", fileName,
                "C:\\Users\\Maxim\\IdeaProjects\\Package_load\\src\\main\\resources\\uploaded_files\\" + fileName);
    }
}
