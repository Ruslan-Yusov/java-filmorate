package ru.yandex.practicum.filmorate.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Objects;

/**
 * DTO для добавления пользователя.
 */
@Data
@AllArgsConstructor
@Builder
public class UserDtoForAdd {
    @NotBlank
    @Email(regexp = "([\\w\\.\\-]*)\\@([\\w\\-]*)\\.(\\p{Lower}{2,4})", message = "Электронный адрес не верный")
    private String email;
    private String login;
    private String name;
    @Past(message = "Дата рождения не может быть в будущем")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDtoForAdd that = (UserDtoForAdd) o;
        return Objects.equals(email, that.email)
                && Objects.equals(login, that.login)
                && Objects.equals(name, that.name)
                && Objects.equals(birthday, that.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
