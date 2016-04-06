package com.kalpesh.locationsapp.utils;

import java.util.List;

/**
 * Created by kalpeshpatel on 06/04/16.
 */
public class ListUtils {

    public static String convetListToString(List<String> strings, String separator) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < strings.size(); i++) {
            builder.append(strings.get(i));
            if (i < strings.size() - 1)
                builder.append(separator);
        }
        return builder.toString();
    }
}
