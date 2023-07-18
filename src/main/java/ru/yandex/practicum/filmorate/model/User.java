package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Класс пользователя.
 */
@Data
@AllArgsConstructor
@Builder
public class User implements GeneratableId<Integer> {
    private Integer id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    public Set<Integer> getFriends() {
        if (friends == null) {
            friends = new HashSet<>();
        }
        return friends;
    }

    private Set<Integer> friends;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
