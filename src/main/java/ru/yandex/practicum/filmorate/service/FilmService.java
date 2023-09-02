package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.api.dto.FilmDtoForAdd;
import ru.yandex.practicum.filmorate.api.dto.FilmDtoForRead;
import ru.yandex.practicum.filmorate.api.dto.SimpleDto;
import ru.yandex.practicum.filmorate.dao.entity.FilmEntity;
import ru.yandex.practicum.filmorate.dao.entity.GenreEntity;
import ru.yandex.practicum.filmorate.dao.repository.FilmRepository;
import ru.yandex.practicum.filmorate.dao.repository.GenreRepository;
import ru.yandex.practicum.filmorate.dao.repository.MpaRepository;
import ru.yandex.practicum.filmorate.exeption.BadRequestException;
import ru.yandex.practicum.filmorate.exeption.ResourceAlreadyExistExeption;
import ru.yandex.practicum.filmorate.exeption.ResourceNotFoundException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

/**
 * Сервис с основной бизнес логикой для класса {@link FilmEntity}.
 */
@Service
@Getter
@Setter
@Slf4j
public class FilmService {

    @Autowired
    private FilmUserMapper mapper;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MpaRepository mpaRepository;

    @Autowired
    private FilmRepository repository;

    private static final String DATE_CONST = "1895-12-28";
    private static final LocalDate DATE_FILM =
            LocalDate.parse(DATE_CONST, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    private boolean checkFilmName(String filmName) {
        return repository.findAll().stream().map(FilmEntity::getName).noneMatch(filmName::equals);
    }

    private boolean checkDateFilm(LocalDate filmDate) {
        return DATE_FILM.isBefore(filmDate);
    }

    public List<FilmDtoForRead> getAllFilmsDto() {
        return mapper.entityToFilmDtoForReadList(repository.findAll());
    }

    public List<FilmEntity> getAllFilms() {
        return repository.findAll();
    }

    public FilmDtoForRead getFilmsDto(Integer id) {
        return repository.findById(id)
                .map(mapper::entityToFilmDtoForRead)
                .orElseThrow(() -> new ResourceNotFoundException("Такого фильма нет"));
    }

    public FilmEntity getFilm(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Такого фильма нет"));
    }

    public FilmDtoForRead addFilm(FilmDtoForAdd filmDto) {
        if (checkFilmName(filmDto.getName())) {
           if (!checkDateFilm(filmDto.getReleaseDate())) {
               throw new BadRequestException(String.format("Дата не должна быть ранее %s", DATE_CONST));
            }
            FilmEntity film = mapper.filmDtoForAddToFilmEntity(filmDto);

            Set<GenreEntity> genres = ofNullable(filmDto.getGenres())
                    .orElseGet(Collections::emptyList)
                    .stream()
                    .map(SimpleDto::getId)
                    .distinct()
                    .map(genreRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            film.setGenres(genres);
            ofNullable(filmDto.getMpa()).map(SimpleDto::getId)
                    .flatMap(mpaRepository::findById)
                    .or(() -> Optional.of(mpaRepository.findAll()).map(m -> m.get(0)))
                    .ifPresent(film::setMpa);
            repository.save(film);
            return mapper.entityToFilmDtoForRead(film);
        } else {
            throw new ResourceAlreadyExistExeption("Такой фильм уже есть в списке");
        }
    }

    @Transactional
    public FilmDtoForRead updateFilm(FilmDtoForRead filmDtoForRead) {
        if (!checkDateFilm(filmDtoForRead.getReleaseDate())) {
            throw new BadRequestException(String.format("Дата не должна быть ранее %s", DATE_CONST));
        }
        FilmEntity film = repository.findById(filmDtoForRead.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Нет такого фильма"));
        Set<GenreEntity> genres = ofNullable(filmDtoForRead.getGenres())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(SimpleDto::getId)
                .distinct()
                .map(genreRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        film.setGenres(genres);

        ofNullable(filmDtoForRead.getMpa()).map(SimpleDto::getId)
                .flatMap(mpaRepository::findById)
                .ifPresent(film::setMpa);
        film.setName(filmDtoForRead.getName());
        film.setDescription(filmDtoForRead.getDescription());
        film.setDuration(filmDtoForRead.getDuration());
        film.setReleaseDate(filmDtoForRead.getReleaseDate());
        log.info("Обновлен фильм: {}", filmDtoForRead.getName());
        return mapper.entityToFilmDtoForRead(repository.save(film));
    }

    public void deleteFilm(Integer id) {
        FilmEntity film = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Такого фильма нет"));
        log.info("Удален фильм {}", film.getName());
        repository.delete(film);
    }

    public void save(FilmEntity film) {
        repository.save(film);
    }
}
