package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.api.dto.FilmDtoForAdd;
import ru.yandex.practicum.filmorate.api.dto.FilmDtoForRead;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FilmController {

    private final FilmService filmService;

    @GetMapping("/films")
    public List<FilmDtoForRead> allFilms() {
        log.info("Вызван метод GetFilms");
        log.info("Количество фильмов: {}", filmService.getItems().size());
        return filmService.getAllFilms();
    }

    @PostMapping("/films")
    public FilmDtoForRead createFilms(@RequestBody @Validated FilmDtoForAdd film) {
        log.info("Вызван метод PostFilms");
        return filmService.addFilm(film);
    }

    @PutMapping("/films")
    public FilmDtoForRead updateFilms(@RequestBody @Validated FilmDtoForRead film) {
        log.info("Вызван метод PutFilms");
        return filmService.updateFilm(film);
    }
}
