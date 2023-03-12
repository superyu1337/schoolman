package com.superyu.datatypes.common;

import java.util.Objects;

public class PhoneNumber {
    String content;

    public PhoneNumber(String number) {
        // worst phone number regex
        if (number.matches("^\\+[0-9]{3}|[0]\\s[0-9]{3}\\s[0-9]{7}$"))
            throw new IllegalArgumentException("Phone number is invalid");

        content = number;
    }

    @Override
    public String toString() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
}
