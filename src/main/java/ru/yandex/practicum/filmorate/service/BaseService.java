package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.GeneratableId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Базовый сервис.
 */

@Getter
@NoArgsConstructor
@Slf4j
public abstract class BaseService<T extends GeneratableId<Integer>> {

    @Autowired
    protected FilmUserMapper mapper;
    protected List<T> items = new ArrayList<>();
    private Integer idGenerate = 0;

    private Integer generateId() {
        return ++idGenerate;
    }

    protected Optional<T> getItem(Integer id) {
        return items.stream().filter(o -> o.getId().equals(id)).findAny();
    }

    protected void add(T item) {
            item.setId(generateId());
            items.add(item);
            log.info("Элемент в список добавлен");
            log.info("В коллекции элементов: {}", items.size());
    }

    public void reset() {
        items.clear();
        idGenerate = 0;
    }
}
