package com.javarush.task.task19.task3310.strategy;

import java.io.Serializable;
import java.util.Objects;

public class Entry implements Serializable {
    int hash;
    Long key;
    String value;
    Entry next;

    public Entry(int hash, Long key, String value, Entry next) {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = next;
    }

    public final Long getKey() {
        return key;
    }

    public final String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getKey()) ^ Objects.hashCode(getValue());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Entry entry = (Entry) obj;

        if (key != null ? !key.equals(entry.key) : entry.key != null) return false;
        return value != null ? value.equals(entry.value) : entry.value == null;
    }

    @Override
    public final String toString() {
        return getKey() + "=" + getValue();
    }
}

