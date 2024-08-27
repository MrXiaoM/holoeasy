package org.holoeasy.util;

public class Pair<K, V> {
    K key;
    V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K component1() {
        return key;
    }

    public V component2() {
        return value;
    }
}
