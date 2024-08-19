package ru.evg.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class HealthErrorDto {

    private static final String SUCCESS_STATUS_STRING = "UP";

    @Getter
    @Setter
    private String errorString;

    @Getter
    @Setter
    private String url;

    public static boolean hasErrors(HealthErrorDto dto) {
        if (dto.getErrorString().equals(SUCCESS_STATUS_STRING)) {
            return false;
        }
        return true;
    }

}
