package com.superyu.datatypes.school;

import com.superyu.datatypes.common.Description;
import com.superyu.datatypes.common.Name;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class SchoolClass implements Comparable<SchoolClass> {
    private final UUID id;
    private SchoolGrade schoolGrade;
    private Name className;
    private Description description;
    private Teacher teacher;
    private HashSet<Student> students;

    public SchoolClass(SchoolGrade schoolGrade, Name className, Description description) {
        this.id = UUID.randomUUID();
        this.schoolGrade = schoolGrade;
        this.className = className;
        this.description = description;
        this.teacher = null;
        this.students = new HashSet<>();
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public HashSet<Student> getStudents() {
        return this.students;
    }

    public SchoolClass addStudent(Student student) {
        this.students.add(student);
        return this;
    }

    public SchoolClass removeStudent(Student student) {
        this.students.remove(student);
        return this;
    }

    public UUID getId() {
        return id;
    }

    public SchoolGrade getSchoolGrade() {
        return schoolGrade;
    }

    public void setSchoolGrade(SchoolGrade schoolGrade) {
        this.schoolGrade = schoolGrade;
    }

    public Name getClassName() {
        return className;
    }

    public void setClassName(Name className) {
        this.className = className;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchoolClass that = (SchoolClass) o;
        return Objects.equals(id, that.id) && Objects.equals(schoolGrade, that.schoolGrade) && Objects.equals(className, that.className) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, schoolGrade, className, description);
    }

    public void print() {
        System.out.println("Name: " + this.className.toString());
        System.out.println("Description: " + this.description.toString());
        System.out.println("Grade: " + this.schoolGrade.toString());
        System.out.println("ID: " + this.id.toString());
        System.out.println("Teacher: " + (this.teacher == null ? "None" : this.teacher.toString()));
        System.out.println("Students:");

        for (Student currStudent : this.students) {
            System.out.println("\t" + currStudent.toString());
        }
    }

    @Override
    public String toString() {
        return className + "(" + schoolGrade + ")";
    }

    @Override
    public int compareTo(SchoolClass schoolClass) {
        return this.className.toString().compareTo(schoolClass.getClassName().toString());
    }
}
