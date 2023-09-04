package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.api.dto.DictionaryDto;
import ru.yandex.practicum.filmorate.dao.repository.GenreRepository;
import ru.yandex.practicum.filmorate.dao.repository.MpaRepository;
import ru.yandex.practicum.filmorate.exeption.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.service.FilmUserMapper;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class DictionaryController {

    private final GenreRepository genreRepository;
    private final MpaRepository mpaRepository;
    private final FilmUserMapper mapper;

    @GetMapping("/genres")
    public List<DictionaryDto> genreGetAll() {
        return genreRepository.findAll().stream()
                .map(mapper::genreEntityToDictionaryDto)
                .sorted(Comparator.comparing(DictionaryDto::getId))
                .collect(Collectors.toList());
    }

    @GetMapping("/mpa")
    public List<DictionaryDto> mpaGetAll() {
        return mpaRepository.findAll().stream()
                .map(mapper::mpaEtityToDictionaryDto)
                .sorted(Comparator.comparing(DictionaryDto::getId))
                .collect(Collectors.toList());
    }

    @GetMapping("/genres/{id}")
    public DictionaryDto genreGetById(@PathVariable("id") Integer id) {
        return genreRepository.findById(id)
                .map(mapper::genreEntityToDictionaryDto)
                .orElseThrow(() -> new ResourceNotFoundException("Нет такого id"));
    }

    @GetMapping("/mpa/{id}")
    public DictionaryDto mpaGetById(@PathVariable("id") Integer id) {
        return mpaRepository.findById(id)
                .map(mapper::mpaEtityToDictionaryDto)
                .orElseThrow(() -> new ResourceNotFoundException("Нет такого id"));
    }
}
