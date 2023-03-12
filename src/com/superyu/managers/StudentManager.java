package com.superyu.managers;

import com.superyu.datatypes.common.Name;
import com.superyu.datatypes.school.Student;
import com.superyu.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

public class StudentManager {
    HashSet<Student> students;

    public StudentManager() {
        this.students = new HashSet<>();
    }

    HashSet<Student> getStudents() {
        return this.students;
    }

    public ArrayList<Student> sortedArrayList() {
        ArrayList<Student> arrayList = new ArrayList<>(this.students);
        Collections.sort(arrayList);
        return arrayList;
    }

    StudentManager addStudent(Student student) {
        this.students.add(student);
        return this;
    }

    StudentManager removeStudent(Student student) {
        this.students.remove(student);
        return this;
    }

    Student getStudentByName(Name name, Name surname) throws NotFoundException {
        for (Student currStudent : this.students) {
            if (currStudent.getName().equals(name) && currStudent.getSurname().equals(surname))
                return currStudent;
        }
        throw new NotFoundException("No student with that name and surname found!");
    }

    Student getStudentByID(UUID id) throws NotFoundException {
        for (Student currStudent : this.students) {
            if (currStudent.getId().equals(id))
                return currStudent;
        }
        throw new NotFoundException("No student with that ID found!");
    }
}
