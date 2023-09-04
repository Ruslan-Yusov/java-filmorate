package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.api.dto.UserDtoForAdd;
import ru.yandex.practicum.filmorate.dao.entity.UserEntity;
import ru.yandex.practicum.filmorate.dao.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        properties = {
                "spring.datasource.url=jdbc:h2:mem:filmorate;MODE=PostgreSQL;INIT=CREATE SCHEMA IF NOT EXISTS filmorate;"
        }
)
class FilmoRateApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private static final LocalDate DATE_HAMSTER = LocalDate.parse("1987-08-04", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    @Test
    void testFindUserById() {
        UserDtoForAdd userDto = UserDtoForAdd.builder()
                .email("byte@yandex.ru")
                .login("hamsterbyte1919")
                .name("Byte")
                .birthday(DATE_HAMSTER).build();
        userService.addUser(userDto);
        Optional<UserEntity> userOptional = userRepository.findById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", user.getId())
                )
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", user.getName())
                )
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("login", user.getLogin())
                );
        Assertions.assertEquals(userDto.getBirthday(), userOptional.map(UserEntity::getBirthday).orElse(null));
    }
}
