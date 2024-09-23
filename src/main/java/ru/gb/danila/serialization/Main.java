package ru.gb.danila.serialization;

import ru.gb.danila.serialization.domain.Student;
import ru.gb.danila.serialization.mapper.ListMapper;
import ru.gb.danila.serialization.mapper.TypeMapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Student> studentList = List.of(
                new Student("ivan", 20, 3.5),
                new Student("peter", 28, 5),
                new Student("dima", 35, 4.5)
        );

        String fileName = "students";
        TypeMapper[] typeMappers = {TypeMapper.BIN, TypeMapper.XML, TypeMapper.JSON};
        List<ListMapper<Student>> mapperList = new ArrayList<>(typeMappers.length);
        for(TypeMapper type: typeMappers){
            mapperList.add(ListMapper.of(fileName, type, Student.class));
        }

        for(ListMapper<Student> mapper: mapperList){
            try {
                mapper.writeList(studentList);
                List<Student> loadedStudents = mapper.readList(Student.class);
                System.out.println(mapper.getFileName());
                System.out.println(loadedStudents);
            }catch (FileNotFoundException e){
                System.err.println("file not found");
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
}
