package ru.gb.danila.serialization.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class JsonListMapper<E extends Serializable> extends AbstractListMapper<E> implements ListMapper<E>{
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }
    /**
     * @param fileName  file name without extension
     */
    public JsonListMapper(String fileName) {
        super(fileName, "json");
    }

    @Override
    public void writeList(List<E> items) throws IOException {
        mapper.writeValue(new File(getFileName()), items);
    }

    @Override
    public List<E> readList(Class<E> itemClass) throws IOException {
        CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, itemClass);
        return (List<E>) mapper.readValue(new File(getFileName()), collectionType);
    }
}
