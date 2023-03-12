package com.superyu.datatypes.common;

import java.util.Objects;

public class Name {
    private final String content;

    public Name(String name) {
        if (!name.matches("^[-äöüa-zA-Z+]+$"))
            throw new IllegalArgumentException("name is invalid");

        content = name;
    }

    @Override
    public String toString() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name = (Name) o;
        return Objects.equals(content, name.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }


}

