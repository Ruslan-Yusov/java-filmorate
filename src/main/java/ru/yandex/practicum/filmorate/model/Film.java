package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
    private Set<Integer> likes;

    public Set<Integer> getLikes() {
        if (likes == null) {
            likes = new HashSet<>();
        }
        return likes;
    }

    public Integer getLikesCount() {
        return getLikes().size();
    }

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
