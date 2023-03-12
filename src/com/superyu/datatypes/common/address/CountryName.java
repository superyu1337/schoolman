package com.superyu.datatypes.common.address;

import java.util.Objects;

public class CountryName {
    private String content;

    public CountryName(String countryName) {
        if (countryName.matches("^\s+$"))
            throw new IllegalArgumentException("Country name is invalid");

        this.content = countryName;
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
        CountryName that = (CountryName) o;
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
