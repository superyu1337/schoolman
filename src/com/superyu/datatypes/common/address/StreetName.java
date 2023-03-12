package com.superyu.datatypes.common.address;

import java.util.Objects;

public class StreetName {
    private String content;

    public StreetName(String streetName) {
        if (streetName.matches("^\s+$"))
            throw new IllegalArgumentException("Street name is invalid");

        this.content = streetName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StreetName that = (StreetName) o;
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public String toString() {
        return content;
    }
}
