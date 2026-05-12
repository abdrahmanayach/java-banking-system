package repository;

import util.JsonFileManager;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public abstract class BaseRepository<T> {
    private final String filePath;
    private final JsonFileManager fileManager;
    private final Type type;

    public BaseRepository(String filePath, JsonFileManager fileManager, Type type) {
        this.filePath = filePath;
        this.fileManager = fileManager;
        this.type = type;
    }

    protected List<T> loadAll() {
        try {
            return fileManager.readFromFile(filePath, type);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from file: " + filePath, e);
        }
    }

    protected void saveAll(List<T> items) {
        try {
            fileManager.writeToFile(filePath, items);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to file: " + filePath, e);
        }
    }
}
