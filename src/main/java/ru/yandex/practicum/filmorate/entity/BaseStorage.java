package ru.yandex.practicum.filmorate.entity;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.GeneratableId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Базовый репозиторий.
 */

@NoArgsConstructor
@Slf4j
public abstract class BaseStorage<T extends GeneratableId<Integer>> {

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

    protected void replace(T oldItem, T newItem) {
        log.info("Замена объекта");
        items.remove(oldItem);
        items.add(newItem);
    }
}
