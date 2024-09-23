package ru.gb.danila.serialization.mapper;

import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class XmlListMapper<E extends Serializable> extends AbstractListMapper<E> implements ListMapper<E>{
    private static final XmlMapper mapper = new XmlMapper();
    /**
     * @param fileName  file name without extension
     */
    public XmlListMapper(String fileName) {
        super(fileName, "xml");
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
