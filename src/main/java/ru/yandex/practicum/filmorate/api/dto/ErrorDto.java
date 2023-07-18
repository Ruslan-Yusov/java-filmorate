package ru.yandex.practicum.filmorate.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 * DTO.
 */
@AllArgsConstructor
@Builder
@Getter
@JsonInclude(value = NON_EMPTY)
public class ErrorDto {
    private Integer code;
    private String message;
    private List<String> details;
}
