package com.superyu.managers;

import com.superyu.datatypes.common.Name;
import com.superyu.datatypes.school.SchoolClass;
import com.superyu.datatypes.school.Student;
import com.superyu.datatypes.school.Teacher;
import com.superyu.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.UUID;

public class SchoolManager {
    private final StudentManager studentManager;
    private final TeacherManager teacherManager;
    private final SchoolClassManager schoolClassManager;

    public SchoolManager() {
        this.studentManager = new StudentManager();
        this.teacherManager = new TeacherManager();
        this.schoolClassManager = new SchoolClassManager();
    }

    // SchoolClassManager wrapper functions
    public ArrayList<SchoolClass> sortedClasses() {
        return this.schoolClassManager.sortedArrayList();
    }

    public SchoolClass getClassByName(Name name) throws NotFoundException {
        return this.schoolClassManager.getClassByName(name);
    }

    public SchoolClass getClassById(UUID id) throws NotFoundException {
        return this.schoolClassManager.getClassByID(id);
    }

    public SchoolManager addClass(SchoolClass schoolClass) throws IllegalArgumentException {
        boolean uniqueName = true;
        boolean uniqueGrade = true;
        for (SchoolClass currClass : this.schoolClassManager.sortedArrayList()) {
            if (currClass.getClassName().equals(schoolClass.getClassName())) {
                uniqueName = false;
                break;
            }

            if (currClass.getSchoolGrade().equals(schoolClass.getSchoolGrade())) {
                uniqueGrade = false;
                break;
            }
        }

        if (!uniqueGrade)
            throw new IllegalArgumentException("A class with that grade already exists.");

        if (!uniqueName)
            throw new IllegalArgumentException("A class with this name already exists.");

        this.schoolClassManager.addSchoolClass(schoolClass);
        return this;
    }

    public SchoolManager removeClass(SchoolClass schoolClass) {
        this.schoolClassManager.removeSchoolClass(schoolClass);
        return this;
    }

    // TeacherManager wrapper functions
    public ArrayList<Teacher> sortedTeachers() {
        return this.teacherManager.sortedArrayList();
    }

    public SchoolManager addTeacher(Teacher teacher) {
        Teacher currTeacher = teacher.getAssignedClass().getTeacher();

        if (currTeacher != null) {
            teacher.getAssignedClass().getTeacher().setAssignedClass(null);
        }

        this.teacherManager.addTeacher(teacher);
        teacher.getAssignedClass().setTeacher(teacher);
        return this;
    }

    public Teacher getTeacherByName(Name name, Name surname) throws NotFoundException {
        return this.teacherManager.getTeacherByName(name, surname);
    }

    public Teacher getTeacherById(UUID id) throws NotFoundException {
        return this.teacherManager.getTeacherByID(id);
    }

    public SchoolManager removeTeacher(Teacher teacher) {
        this.teacherManager.removeTeacher(teacher);
        teacher.getAssignedClass().setTeacher(null);
        return this;
    }

    // StudentManager wrapper functions
    public ArrayList<Student> sortedStudents() {
        return this.studentManager.sortedArrayList();
    }

    public SchoolManager addStudent(Student student) {
        this.studentManager.addStudent(student);
        student.getAssignedClass().addStudent(student);
        return this;
    }

    public Student getStudentByName(Name name, Name surname) throws NotFoundException {
        return this.studentManager.getStudentByName(name, surname);
    }

    public Student getStudentById(UUID id) throws NotFoundException {
        return this.studentManager.getStudentByID(id);
    }

    public SchoolManager removeStudent(Student student) {
        this.studentManager.removeStudent(student);
        student.getAssignedClass().removeStudent(student);
        return this;
    }
}
