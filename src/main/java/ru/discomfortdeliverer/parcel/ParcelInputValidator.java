package ru.discomfortdeliverer.parcel;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.regex.Pattern;

@Slf4j
public class ParcelInputValidator {
    private static final Pattern pattern = Pattern.compile("^(999|8888|777|7777|666|55555|4444|333|22|1)$");

    public boolean isValidListOfLines(List<String> lines) {
        for (String line : lines) {
            if (line.isEmpty()) {
                continue;
            }
            if (!isValidLine(line)) {
                log.debug("Невалидная строка - {}", line);
                return false;
            }
        }
        return true;
    }

    private boolean isValidLine(String line) {
        return pattern.matcher(line).matches();
    }
}
