package ru.gb.danila.serialization.mapper;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public interface ListMapper<E extends Serializable> {
    static <E extends Serializable> ListMapper<E> of(String fileName, TypeMapper typeMapper, Class<E> classItem){
        return switch (typeMapper){
            case BIN -> new BinListMapper<>(fileName);
            case XML -> new XmlListMapper<>(fileName);
            case JSON -> new JsonListMapper<>(fileName);
        };
    }
    void writeList(List<E> items) throws IOException;

    List<E> readList(Class<E> itemClass) throws IOException;

    String getFileName();
}
