package ru.gb.danila.serialization.mapper;

import java.io.File;
import java.io.Serializable;

public abstract class AbstractListMapper<E extends Serializable> implements  ListMapper<E>{
    private final String fileName;

    /**
     *
     * @param fileName file name without extension
     * @param extension extension of file
     */
    public AbstractListMapper(String fileName, String extension) {
        this.fileName = fileName + "." + extension;
    }

    @Override
    public String getFileName() {
        return fileName;
    }
}
