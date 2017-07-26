package me.frostythedev.privatecontainers.storage;

public interface DataStorage<T> {

    void setup();
    T load(String name);
    void save(T t);
    boolean remove(T t);
}
