package ru.yandex.practicum.filmorate.model;

public interface GeneratableId<I> {
    void setId(I id);

    I getId();
}
