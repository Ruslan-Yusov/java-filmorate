package ru.yandex.practicum.filmorate.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 * DTO фильма для чтения и обновления.
 */
@Data
@AllArgsConstructor
@JsonInclude(value = NON_EMPTY)
@Builder
public class FilmDtoForRead {
    @Positive
    private Integer id;
    @NotBlank(message = "Наименование фильма не должно быть пустым")
    private String name;
    @Size(max = 200, message = "Описание фильма не должно быть больше 200 символов")
    private String description;
    @Past(message = "Дата релиза не должна быть в будущем")
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @Positive(message = "Длинна фильма должна быть положительной")
    private Double duration;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilmDtoForRead that = (FilmDtoForRead) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(releaseDate, that.releaseDate)
                && Objects.equals(String.format("%.2f", duration), String.format("%.2f",that.duration));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
