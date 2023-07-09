package ru.yandex.practicum.filmorate.exeption;

public class BadRequestException extends BaseException {
    public BadRequestException(String message) {
        super(message);
    }
}
