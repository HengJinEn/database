package com.example.database.DataStructure;

// MyHashMap.java

import java.io.Serializable;

public class MyHashMap<K, V> implements Serializable {

    private class Entry implements Serializable{
        K key;
        V value;
        Entry next;
        Entry prev;
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private int size;
    MyLinkedList<Entry> [] buckets;

    // Head and tail pointers for maintaining the order
    private Entry head;
    private Entry tail;

    private static final double LOAD_FACTOR_THRESHOLD = 0.75;
    private static final int DEFAULT_CAPACITY = 16;

    @SuppressWarnings("unchecked")
    public MyHashMap(){
        buckets = new MyLinkedList[DEFAULT_CAPACITY];
        for (int i = 0; i < DEFAULT_CAPACITY; i++) {
            buckets[i] = new MyLinkedList<>();
        }

        size = 0;
    }

    int hashFunction(K key){
        return Math.abs(key.hashCode() % buckets.length);
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        MyLinkedList<Entry>[] newBuckets = new MyLinkedList[newCapacity];
        for (int i = 0; i < newCapacity; i++) {
            newBuckets[i] = new MyLinkedList<>();
        }

        // Rehash all entries into the new buckets
        for (MyLinkedList<Entry> bucket : buckets) {
            for (int i = 0; i < bucket.size(); i++) {
                Entry entry = bucket.get(i);
                int newIndex = entry.key.hashCode() % newCapacity;
                newBuckets[newIndex].add(entry);
            }
        }

        // Update the buckets and size
        buckets = newBuckets;
    }
    public void put(K key, V value) {
        int index = hashFunction(key);
        MyLinkedList<Entry> bucket = buckets[index];

        for (int i = 0; i < bucket.size(); i++) {
            Entry entry = bucket.get(i);
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }

        Entry newEntry = new Entry(key, value);
        bucket.add(newEntry);
        size++;

        // Update the linked list for maintaining order
        if (tail == null) {
            head = newEntry;
            tail = newEntry;
        } else {
            newEntry.prev = tail;
            tail.next = newEntry;
            tail = newEntry;
        }

        double loadFactor = (double) size / buckets.length;
        if (loadFactor > LOAD_FACTOR_THRESHOLD) {
            int newCapacity = buckets.length * 2;
            resize(newCapacity);
        }
    }


    public V get(K key) {
        int index = hashFunction(key);
        MyLinkedList<Entry> bucket = buckets[index];

        for (int i = 0; i < bucket.size(); i++) {
            Entry entry = bucket.get(i);
            if (entry.key.equals(key)) {
                return entry.value;
            }

        }
        return null;
    }


    public V remove(K key) {
        int index = hashFunction(key);
        MyLinkedList<Entry> bucket = buckets[index];

        for (int i = 0; i < bucket.size(); i++) {
            Entry entry = bucket.get(i);
            if (entry.key.equals(key)) {
                V removedValue = entry.value;

                if (entry.prev != null) {
                    entry.prev.next = entry.next;
                } else {
                    head = entry.next;
                }

                if (entry.next != null) {
                    entry.next.prev = entry.prev;
                } else {
                    tail = entry.prev;
                }

                bucket.remove(i);
                size--;

                double loadFactor = (double) size / buckets.length;
                if (loadFactor > LOAD_FACTOR_THRESHOLD) {
                    int newCapacity = buckets.length * 2;
                    resize(newCapacity);
                }

                return removedValue;
            }
        }
        return null;
    }


    public MyLinkedList<K> keySet(){
        MyLinkedList<K> keys = new MyLinkedList<>();

        Entry current = head;
        while (current != null) {
            keys.add(current.key);
            current = current.next;
        }

        return keys;
    }
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        for (MyLinkedList<Entry> bucket : buckets) {
            bucket.clear();
        }
        size = 0;
    }

}

