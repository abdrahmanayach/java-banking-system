package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import model.Account;
import model.CheckingAccount;
import model.SavingsAccount;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class JsonFileManager {
    private final Gson gson;

    public JsonFileManager() {
        RuntimeTypeAdapterFactory<Account> accountAdapter = RuntimeTypeAdapterFactory
                .of(Account.class, "accountType", true)
                .registerSubtype(SavingsAccount.class, "SAVINGS")
                .registerSubtype(CheckingAccount.class, "CHECKING");

        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapterFactory(accountAdapter)
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter out, LocalDateTime value) throws IOException {
                        if (value == null) {
                            out.nullValue();
                            return;
                        }
                        out.value(value.toString());
                    }

                    @Override
                    public LocalDateTime read(JsonReader in) throws IOException {
                        if (in.peek() == JsonToken.NULL) {
                            in.nextNull();
                            return null;
                        }
                        return LocalDateTime.parse(in.nextString());
                    }
                })
                .registerTypeAdapter(LocalDate.class, new TypeAdapter<LocalDate>() {
                    @Override
                    public void write(JsonWriter out, LocalDate value) throws IOException {
                        if (value == null) {
                            out.nullValue();
                            return;
                        }
                        out.value(value.toString());
                    }

                    @Override
                    public LocalDate read(JsonReader in) throws IOException {
                        if (in.peek() == JsonToken.NULL) {
                            in.nextNull();
                            return null;
                        }
                        return LocalDate.parse(in.nextString());
                    }
                })
                .create();
    }

    public void writeToFile(String filePath, Object data) throws IOException {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            boolean created = parent.mkdirs();
            if (!created) {
                throw new IOException("Failed to create directories: " + parent);
            }
        }
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(data, writer);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T readFromFile(String filePath, Type type) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            return (T) new ArrayList<>();
        }
        try (FileReader reader = new FileReader(file)) {
            T res = gson.fromJson(reader, type);
            if (res == null) {
                return (T) new ArrayList<>();
            }
            return res;
        }
    }
}
