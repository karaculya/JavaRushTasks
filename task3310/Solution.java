package com.javarush.task.task19.task3310;

import com.javarush.task.task33.task3310.strategy.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Solution {
    public static void main(String[] args) {
        long elementsNumber = 10000;

        testStrategy(new HashMapStorageStrategy(), elementsNumber);

        testStrategy(new FileStorageStrategy(), elementsNumber);

        testStrategy(new OurHashMapStorageStrategy(), elementsNumber);

        testStrategy(new OurHashBiMapStorageStrategy(), elementsNumber);
    }

    public static void testStrategy(StorageStrategy strategy, long elementsNumber) {
        Helper.printMessage(strategy.getClass().getSimpleName() + ":");

        Set<String> originalStrings = new HashSet<>();

        for (int i = 0; i < elementsNumber; ++i) {
            originalStrings.add(Helper.generateRandomString());
        }

        Shortener shortener = new Shortener(strategy);

        Date startTime = new Date();
        Set<Long> keys = getIds(shortener, originalStrings);
        Date endTime = new Date();
        long time = endTime.getTime() - startTime.getTime();
        Helper.printMessage("Время получения идентификаторов для " + elementsNumber + " строк: " + time);

        startTime = new Date();
        Set<String> strings = getStrings(shortener, keys);
        endTime = new Date();
        time = endTime.getTime() - startTime.getTime();
        Helper.printMessage("Время получения идентификаторов для " + elementsNumber + " строк: " + time);

        if (originalStrings.equals(strings))
            Helper.printMessage("Тест пройден.");
        else
            Helper.printMessage("Тест не пройден.");
    }

    public static Set<Long> getIds(Shortener shortener, Set<String> strings) {
        Set<Long> keys = new HashSet<>();
        for (String value : strings)
            keys.add(shortener.getId(value));
        return keys;
    }

    public static Set<String> getStrings(Shortener shortener, Set<Long> keys) {
        Set<String> strings = new HashSet<>();
        for (Long id : keys)
            strings.add(shortener.getString(id));
        return strings;
    }
}
