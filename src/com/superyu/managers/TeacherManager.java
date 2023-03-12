package com.superyu.managers;

import com.superyu.datatypes.common.Name;
import com.superyu.datatypes.school.Student;
import com.superyu.datatypes.school.Teacher;
import com.superyu.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

public class TeacherManager {
    private HashSet<Teacher> teachers;

    public TeacherManager() {
        this.teachers = new HashSet<Teacher>();
    }

    HashSet<Teacher> getTeachers() {
        return this.teachers;
    }

    public ArrayList<Teacher> sortedArrayList() {
        ArrayList<Teacher> arrayList = new ArrayList<>(this.teachers);
        Collections.sort(arrayList);
        return arrayList;
    }

    TeacherManager addTeacher(Teacher teacher) {
        this.teachers.add(teacher);
        return this;
    }

    TeacherManager removeTeacher(Teacher teacher) {
        this.teachers.remove(teacher);
        return this;
    }

    Teacher getTeacherByName(Name name, Name surname) throws NotFoundException {
        for (Teacher currTeacher : this.teachers) {
            if (currTeacher.getName().equals(name) && currTeacher.getSurname().equals(surname))
                return currTeacher;
        }
        throw new NotFoundException("No teacher with that name and surname found!");
    }

    Teacher getTeacherByID(UUID id) throws NotFoundException {
        for (Teacher currTeacher : this.teachers) {
            if (currTeacher.getId().equals(id))
                return currTeacher;
        }
        throw new NotFoundException("No teacher with that ID found!");
    }
}
