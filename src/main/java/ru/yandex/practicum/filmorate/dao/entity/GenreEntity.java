package ru.yandex.practicum.filmorate.dao.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "genre")
public class GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;
}
