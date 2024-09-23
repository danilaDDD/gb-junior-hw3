package ru.gb.danila.jdbc;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Person {
    private long id;
    private String name;
    private int age;
    private boolean active;
    private Department department;

    public Person(Long id, String name, int age, Department department){
        this(id, name, age, true, department);
    }
}
