package com.superyu.datatypes.common;

public class Description {
    private String content;

    public Description(String description) {
        if (description.matches("^\s+$"))
            throw new IllegalArgumentException("description is invalid");

        this.content = description;
    }

    @Override
    public String toString() {
        return this.content;
    }
}
