package ru.gb.danila.serialization.mapper;

import java.io.*;
import java.util.List;

public class BinListMapper<E extends Serializable> extends AbstractListMapper<E> implements ListMapper<E> {

    public BinListMapper(String fileName) {
        super(fileName, "bin");
    }

    @Override
    public void writeList(List<E> items) throws IOException {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(getFileName()))) {
            oos.writeObject(items);
        }

    }

    @Override
    public List<E> readList(Class<E> itemClass) throws IOException {
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(getFileName()))) {
            return (List<E>) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
