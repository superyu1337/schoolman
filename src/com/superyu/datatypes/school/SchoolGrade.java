package com.superyu.datatypes.school;

import java.util.Objects;

public class SchoolGrade {
    private String content;

    public SchoolGrade(String schoolGrade) {
        if (!schoolGrade.matches("^[0-9]{1,2}[a-z]$"))
            throw new IllegalArgumentException("School grade is invalid");

        this.content = schoolGrade;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if (!content.matches("^[0-9]{1,2}[a-z]$"))
            throw new IllegalArgumentException();

        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchoolGrade that = (SchoolGrade) o;
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public String toString() {
        return this.content;
    }
}
