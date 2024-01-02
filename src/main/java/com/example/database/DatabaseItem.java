package com.example.database;

import java.util.Date;

public class DatabaseItem<T> {
    private final T value;

    public DatabaseItem(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public String getType() {
        return value.getClass().getSimpleName();
    }
}

class StringItem extends DatabaseItem<String> {
    public StringItem(String value) {
        super(value);
    }

    @Override
    public String toString() {
        return getValue();
    }
}

class CharItem extends DatabaseItem<Character> {
    public CharItem(char value) {
        super(value);
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }
}

class NumericItem extends DatabaseItem<Double> {
    public NumericItem(double value) {
        super(value);
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }
}


class CollectionItem<T> extends DatabaseItem<T[]>{

    public CollectionItem(T[] value) {
        super(value);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < getValue().length; i++) {
            result.append(getValue()[i]);
            if (i < getValue().length - 1) {
                result.append(", ");
            }
        }

        result.append("]");

        return result.toString();
    }
}

class BooleanItem extends DatabaseItem<Boolean> {
    public BooleanItem(boolean value) {
        super(value);
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }
}

class DateItem extends DatabaseItem<Date> {
    public DateItem(Date value) {
        super(value);
    }

    @Override
    public String toString() {
        return getValue().toString();
    }
}