package com.tenggyut.common.utils;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tenggyut.common.logging.LogFactory;
import org.apache.logging.log4j.Logger;

/**
 * Json utility class
 * <p/>
 * Created by tenggyt on 2015/8/20.
 */
public class JsonUtils {
    private static final Logger LOG = LogFactory.getLogger(JsonUtils.class);

    private static final JsonParser gsonParser = new JsonParser();

    public static JsonObject parseObjWithGson(String json) {
        return gsonParser.parse(json).getAsJsonObject();
    }

    public static JsonArray parseArrayWithGson(String json) {
        return gsonParser.parse(json).getAsJsonArray();
    }
}
