package com.superyu.datatypes.common.address;

import java.util.Objects;

public class ZipCode {
    private String content;

    public ZipCode(String zipCode) {
        if (!zipCode.matches("^[a-zA-Z0-9+]+$"))
            throw new IllegalArgumentException("Zipcode is invalid");

        this.content = zipCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if (!content.matches("^[a-zA-Z0-9+]+$"))
            throw new IllegalArgumentException("Zipcode is invalid");

        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZipCode zipCode = (ZipCode) o;
        return Objects.equals(content, zipCode.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public String toString() {
        return String.valueOf(content);
    }
}
