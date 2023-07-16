package ru.yandex.practicum.filmorate.entity;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Component
public class InMemoryUserStorage extends BaseStorage<User> implements UserStorage {
    @Override
    public void add(User user) {
        super.add(user);
    }

    @Override
    public void remove(User foundUser) {
        items.remove(foundUser);
    }

    @Override
    public Optional<User> getById(Integer id) {
        return items.stream().filter(user -> user.getId().equals(id)).findAny();
    }

    @Override
    public List<User> getAll() {
        return items;
    }

    @Override
    public void replace(User oldItem, User newItem) {
        super.replace(oldItem, newItem);
    }
}
