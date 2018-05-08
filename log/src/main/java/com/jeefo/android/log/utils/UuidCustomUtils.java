package com.jeefo.android.log.utils;

import java.util.UUID;

/**
 * Created by Alex on 02/01/18.
 */

public class UuidCustomUtils {
    /**
     * Static method used for generating an unique identifier
     * @return the generated uid
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Static method used for generating a 6 digits identifier
     * @return the generated identifier
     */
    public static String generateShortUUID() {
        return UUID.randomUUID().toString().substring(0, 6);
    }
}