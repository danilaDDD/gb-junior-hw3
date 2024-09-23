package ru.gb.danila.serialization.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.*;

@Getter
@Setter
@AllArgsConstructor
public class Student implements Serializable {
    private String name;
    private int age;
    transient double GPA;

    public Student() {
    }

    @Override
    public String toString() {
        return String.format("Student{%s, %s, %s}", name, age, GPA);
    }
}
