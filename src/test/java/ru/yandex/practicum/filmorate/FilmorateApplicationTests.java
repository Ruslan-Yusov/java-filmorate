package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.api.dto.FilmDtoForAdd;
import ru.yandex.practicum.filmorate.api.dto.FilmDtoForRead;
import ru.yandex.practicum.filmorate.api.dto.UserDtoForAdd;
import ru.yandex.practicum.filmorate.api.dto.UserDtoForRead;
import ru.yandex.practicum.filmorate.entity.FilmStorage;
import ru.yandex.practicum.filmorate.entity.UserStorage;
import ru.yandex.practicum.filmorate.exeption.BadRequestException;
import ru.yandex.practicum.filmorate.exeption.ResourceAlreadyExistExeption;
import ru.yandex.practicum.filmorate.exeption.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest
class FilmorateApplicationTests {

    @Autowired
    private FilmService filmService;

    @Autowired
    private UserService userService;

    @Autowired
    private FilmStorage filmStorage;

    @Autowired
    private UserStorage userStorage;

    private static final LocalDate DATE_HAMSTER = LocalDate.parse("1987-08-04", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    private static final String DATE_CONST = "1995-12-28";
    private static final LocalDate DATE_FILM = LocalDate.parse(DATE_CONST, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    private static Film expectetFilm;

    private static FilmDtoForAdd expectetFilmDtoForAdd;

    private static FilmDtoForRead expectetFilmDtoForRead;

    private static FilmDtoForRead expectetUpdateFilmDtoForRead;

    private static User expectetUser;

    private static UserDtoForAdd expectetUserDtoForAdd;

    private static UserDtoForRead expectetUserDtoForRead;

    private static UserDtoForRead expectetUpdateUserDtoForUpdate;

    @BeforeEach
    public void beforeEach() {

        expectetFilm = Film.builder().id(1).name("Хомяк Байт").description("Жизнь байта").releaseDate(DATE_FILM).duration(new BigDecimal("2.15")).build();

        expectetFilmDtoForAdd = FilmDtoForAdd.builder().name("Хомяк Байт").description("Жизнь байта").releaseDate(DATE_FILM).duration(2.15).build();

        expectetFilmDtoForRead = FilmDtoForRead.builder().id(1).name("Хомяк Байт").description("Жизнь байта").releaseDate(DATE_FILM).duration(2.15).build();

        expectetUpdateFilmDtoForRead = FilmDtoForRead.builder().id(1).name("Хомяк Байт 2").description("Смерть байта").releaseDate(DATE_FILM).duration(2.17).build();

        expectetUser = User.builder().id(1).email("byte@yandex.ru").login("hamsterbyte1919").name("Byte").birthday(DATE_HAMSTER).build();

        expectetUserDtoForAdd = UserDtoForAdd.builder().name("Byte").email("byte@yandex.ru").login("hamsterbyte1919").birthday(DATE_HAMSTER).build();

        expectetUserDtoForRead = UserDtoForRead.builder().id(1).email("byte@yandex.ru").login("hamsterbyte1919").name("Byte").birthday(DATE_HAMSTER).build();

        expectetUpdateUserDtoForUpdate = UserDtoForRead.builder().id(1).email("byte@mail.ru").login("hamsterbyte1919").name("Byte").birthday(DATE_HAMSTER).build();
        filmStorage.reset();
        userStorage.reset();
    }

    @Test
    @DisplayName("Проверка позитивного сценария")
    void testFilms() {
        Assertions.assertEquals(0, filmService.getAllFilms().size());

        FilmDtoForRead variable = filmService.addFilm(expectetFilmDtoForAdd);
        Assertions.assertNotNull(variable);
        Assertions.assertEquals(expectetFilmDtoForRead, variable);

        List<Film> innerList = filmStorage.getAll();
        Assertions.assertNotNull(innerList);
        Assertions.assertNotEquals(0, innerList.size());
        Assertions.assertEquals(expectetFilm, innerList.get(0));

        List<FilmDtoForRead> filmList = filmService.getAllFilmsDto();
        Assertions.assertEquals(1, filmList.size());
        Assertions.assertEquals(expectetFilmDtoForRead, filmList.get(0));

        FilmDtoForRead updateFilm = filmService.updateFilm(expectetUpdateFilmDtoForRead);
        Assertions.assertNotNull(updateFilm);
        Assertions.assertEquals(expectetUpdateFilmDtoForRead, updateFilm);
    }

    @Test
    @DisplayName("Тест на дату")
    void testValidationBadDate() {
        expectetFilmDtoForAdd.setReleaseDate(DATE_FILM.minusYears(500));
        Assertions.assertThrows(BadRequestException.class, () -> filmService.addFilm(expectetFilmDtoForAdd));
    }

    @Test
    @DisplayName("Тест на двойное добавление и обновления с неизвестным id")
    void testValidationTwoAddFilmAndUpdateBad() {
        FilmDtoForRead variable = filmService.addFilm(expectetFilmDtoForAdd);
        Assertions.assertThrows(ResourceAlreadyExistExeption.class, () -> filmService.addFilm(expectetFilmDtoForAdd));
        expectetFilmDtoForRead.setId(4);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> filmService.updateFilm(expectetFilmDtoForRead));
    }

    @Test
    @DisplayName("Тест на удаление фильма")
    void testDeleteFilms() {
        FilmDtoForRead variable = filmService.addFilm(expectetFilmDtoForAdd);
        Assertions.assertEquals(1, filmStorage.getAll().size());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> filmService.deleteFilm(2));
        filmService.deleteFilm(1);
        Assertions.assertEquals(0, filmStorage.getAll().size());
    }

    @Test
    @DisplayName("Проверка позитивного сценария пользователя")
    void testUser() {
        Assertions.assertEquals(0, userService.getAllUsers().size());

        UserDtoForRead variable = userService.addUsers(expectetUserDtoForAdd);
        Assertions.assertNotNull(variable);
        Assertions.assertEquals(expectetUserDtoForRead, variable);

        List<User> innerList = userStorage.getAll();
        Assertions.assertNotNull(innerList);
        Assertions.assertNotEquals(0, innerList.size());
        Assertions.assertEquals(expectetUser, innerList.get(0));

        List<UserDtoForRead> userList = userService.getAllUsers();
        Assertions.assertEquals(1, userList.size());
        Assertions.assertEquals(expectetUserDtoForRead, userList.get(0));

        UserDtoForRead updateUser = userService.updateUser(expectetUpdateUserDtoForUpdate);
        Assertions.assertNotNull(updateUser);
        Assertions.assertEquals(expectetUpdateUserDtoForUpdate, updateUser);
    }

    @Test
    @DisplayName("Тест на двойное добавление и обновления с неизвестным id")
    void testValidationTwoAddUserAndUpdateBad() {
        UserDtoForRead variable = userService.addUsers(expectetUserDtoForAdd);
        Assertions.assertThrows(ResourceAlreadyExistExeption.class, () -> userService.addUsers(expectetUserDtoForAdd));
        expectetUserDtoForRead.setId(4);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(expectetUserDtoForRead));
    }

    @Test
    @DisplayName("Тест на неверный логин пользователя")
    void testBadLoginUser() {
        expectetUserDtoForAdd.setLogin("hams terbyte1919");
        Assertions.assertThrows(BadRequestException.class, () -> userService.addUsers(expectetUserDtoForAdd));
        expectetUserDtoForAdd.setLogin("");
        Assertions.assertThrows(BadRequestException.class, () -> userService.addUsers(expectetUserDtoForAdd));
    }

    @Test
    @DisplayName("Тест на пустое имя")
    void testBlankNameUser() {
        expectetUserDtoForAdd.setName("");
        UserDtoForRead userBlankName = userService.addUsers(expectetUserDtoForAdd);
        Assertions.assertEquals(expectetUserDtoForRead.getLogin(), userBlankName.getName());
    }

    @Test
    @DisplayName("Тест на удаление пользователя")
    void testDeleteUser() {
        UserDtoForRead variable = userService.addUsers(expectetUserDtoForAdd);
        Assertions.assertEquals(1, userStorage.getAll().size());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(2));
        userService.deleteUser(1);
        Assertions.assertEquals(0, userStorage.getAll().size());
    }
}
