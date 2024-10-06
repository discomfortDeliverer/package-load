package ru.discomfortdeliverer.bot;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class MessageService {
    public SendMessage messageReceiver(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String firstName = update.getMessage().getChat().getFirstName();

            String responseText;
            switch (text) {
                case "/start":
                    responseText = "Привет";
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
}
