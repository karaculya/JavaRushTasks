package com.javarush.task.task19.task3310;

import com.javarush.task.task33.task3310.strategy.StorageStrategy;

public class Shortener implements StorageStrategy {
    private Long lastId = 0L;
    private StorageStrategy storageStrategy;

    public Shortener(StorageStrategy storageStrategy) {
        this.storageStrategy = storageStrategy;
    }

    synchronized public Long getId(String string){
        if (storageStrategy.containsValue(string))
            return storageStrategy.getKey(string);
        else {
            lastId ++;
            storageStrategy.put(lastId, string);
            return lastId;
        }
    }

    synchronized public String getString(Long id){
        return storageStrategy.getValue(id);
    }

    @Override
    public boolean containsKey(Long key) {
        return false;
    }

    @Override
    public boolean containsValue(String value) {
        return false;
    }

    @Override
    public void put(Long key, String value) {

    }

    @Override
    public Long getKey(String value) {
        return null;
    }

    @Override
    public String getValue(Long key) {
        return null;
    }
}
