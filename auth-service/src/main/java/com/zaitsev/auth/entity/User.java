package com.zaitsev.auth.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity //- таблица в БД
@Table(name = "users") // - имя таблицы в БД
@Getter
@Setter
@NoArgsConstructor // - конструктор пуст
@AllArgsConstructor // - конструктор все поля
@Builder // - ломбок под капотом
public class User {

    @Id // - первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY) // - генерация ключа (1,2,3...н...)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    private String lastName;

    private String middleName;

    private String birthDate;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;



    @Column(nullable = false)
    private String phone;
}