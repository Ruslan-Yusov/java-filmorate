package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.api.dto.FilmDtoForAdd;
import ru.yandex.practicum.filmorate.api.dto.FilmDtoForRead;
import ru.yandex.practicum.filmorate.service.FilmLikesService;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

/**
 * Contrller.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class FilmController {

    private final FilmService filmService;
    private final FilmLikesService filmLikesService;

    @GetMapping("/echo")
    public String echo() {
        return "Ok";
    }

    @GetMapping("/films")
    public List<FilmDtoForRead> allFilms() {
        log.info("Вызван метод GetFilms");
        log.info("Количество фильмов: {}", filmService.getAllFilms().size());
        return filmService.getAllFilmsDto();
    }

    @GetMapping("/films/{id}")
    public FilmDtoForRead film(@PathVariable("id") Integer id) {
        return filmService.getFilmsDto(id);
    }

    @GetMapping("/films/popular")
    public List<FilmDtoForRead> getLikesFilm(@RequestParam(value = "count", required = false, defaultValue = "10")
                                         Integer count) {
        return filmLikesService.getLikesFilm(count);
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

    @PutMapping("/films/{id}/like/{userId}")
    public void addLikesFilm(@PathVariable("id") Integer id,
                             @PathVariable("userId") Integer userId) {
        filmLikesService.addLikes(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLikesfilm(@PathVariable("id") Integer id,
                                @PathVariable("userId") Integer userId) {
        filmLikesService.deleteLikes(id, userId);
    }

    @DeleteMapping("/films/{id}")
    public void deleteFilms(@PathVariable("id") Integer id) {
        filmService.deleteFilm(id);
    }
}
