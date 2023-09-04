package ru.yandex.practicum.filmorate.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.entity.MpaEntity;

import java.util.Optional;

@Repository
public interface MpaRepository extends JpaRepository<MpaEntity, Integer> {
    Optional<MpaEntity> findByName(String name);
}
