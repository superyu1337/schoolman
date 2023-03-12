package com.superyu.datatypes.common.address;

import java.util.Objects;

public class CityName {
    private String content;

    public CityName(String cityName) {
        if (cityName.matches("^\s+$"))
            throw new IllegalArgumentException("City name is invalid");

        this.content = cityName;
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
        CityName cityName = (CityName) o;
        return Objects.equals(content, cityName.content);
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
