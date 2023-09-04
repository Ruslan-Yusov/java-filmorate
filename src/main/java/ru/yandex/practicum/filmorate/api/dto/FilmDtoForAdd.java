package ru.yandex.practicum.filmorate.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * DTO фильма для добавления.
 */
@Data
@AllArgsConstructor
@Builder
public class FilmDtoForAdd {
    @NotBlank(message = "Наименование фильма не должно быть пустым")
    private String name;
    @Size(max = 200, message = "Описание фильма не должно быть больше 200 символов")
    private String description;
    @Past(message = "Дата релиза не должна быть в будущем")
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @Positive(message = "Длина фильма должна быть положительной")
    private Double duration;

    private List<SimpleDto> genres;

    private SimpleDto mpa;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilmDtoForAdd film = (FilmDtoForAdd) o;
        return Objects.equals(name, film.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
