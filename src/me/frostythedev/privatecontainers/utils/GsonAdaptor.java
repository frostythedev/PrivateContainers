package me.frostythedev.privatecontainers.utils;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

public interface GsonAdaptor<T> extends JsonSerializer<T>, JsonDeserializer<T> {
}
