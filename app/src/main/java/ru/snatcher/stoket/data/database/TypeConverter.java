package ru.snatcher.stoket.data.database;

import java.util.Collections;
import java.util.List;

public class TypeConverter {
    @android.arch.persistence.room.TypeConverter
    public static List<String> stringToUrlList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(data);
    }

    @android.arch.persistence.room.TypeConverter
    public static String urlListToString(List<String> urls) {
        return urls.get(0);
    }
}