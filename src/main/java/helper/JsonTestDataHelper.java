package helper;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonTestDataHelper {

    public static <T> Object[][] getTestData(String filePath, Class<T[]> type){
        T[] items;
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {
            items = new Gson().fromJson(reader, type);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo leer " + filePath, e);
        }

        Object[][] data = new Object[items.length][1];
        for (int i = 0; i < items.length; i++) {
            data[i][0] = items[i];
        }
        return data;
    }
}
