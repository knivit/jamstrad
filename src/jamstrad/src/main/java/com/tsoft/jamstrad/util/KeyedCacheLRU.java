package com.tsoft.jamstrad.util;

import java.util.List;
import java.util.Vector;

public class KeyedCacheLRU<K, V> {
    private int capacity;
    private List<K> keysLRU;
    private List<V> valuesLRU;

    public KeyedCacheLRU(int capacity) {
        this.capacity = Math.max(capacity, 1);
        this.keysLRU = new Vector(capacity);
        this.valuesLRU = new Vector(capacity);
    }

    public int size() {
        return this.getKeysLRU().size();
    }

    public synchronized void clear() {
        this.getKeysLRU().clear();
        this.getValuesLRU().clear();
    }

    public synchronized void storeInCache(K key, V value) {
        if (!this.containsKey(key)) {
            if (this.size() == this.getCapacity()) {
                this.evictOne();
            }

            this.getKeysLRU().add(key);
            this.getValuesLRU().add(value);
        }

    }

    public synchronized boolean containsKey(K key) {
        return this.getKeysLRU().contains(key);
    }

    public synchronized V fetchFromCache(K key) {
        V value = null;
        int index = this.getKeysLRU().indexOf(key);
        if (index >= 0) {
            value = this.getValuesLRU().get(index);
            if (index < this.size() - 1) {
                this.getKeysLRU().add(this.getKeysLRU().remove(index));
                this.getValuesLRU().add(this.getValuesLRU().remove(index));
            }
        }

        return value;
    }

    private void evictOne() {
        K key = this.getKeysLRU().remove(0);
        V value = this.getValuesLRU().remove(0);
        this.evicted(key, value);
    }

    protected void evicted(K key, V value) {
    }

    public int getCapacity() {
        return this.capacity;
    }

    private List<K> getKeysLRU() {
        return this.keysLRU;
    }

    private List<V> getValuesLRU() {
        return this.valuesLRU;
    }
}
