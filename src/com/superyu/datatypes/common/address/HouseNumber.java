package com.superyu.datatypes.common.address;

import java.util.Objects;

public class HouseNumber {
    private int content;

    public HouseNumber(int houseNumber) {
        if (houseNumber <= 0)
            throw new IllegalArgumentException("House number is invalid");

        this.content = houseNumber;
    }

    public int getContent() {
        return content;
    }

    public void setContent(int content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HouseNumber that = (HouseNumber) o;
        return content == that.content;
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
