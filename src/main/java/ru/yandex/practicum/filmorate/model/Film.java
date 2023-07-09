package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Класс для модели о фильмах.
 */
@Data
@AllArgsConstructor
@Builder
public class Film implements GeneratableId<Integer> {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private BigDecimal duration;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return Objects.equals(id, film.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
