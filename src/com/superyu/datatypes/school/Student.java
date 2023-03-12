package com.superyu.datatypes.school;

import com.superyu.datatypes.common.Name;

public class Student extends SchoolPerson {
    public Student(Name name, Name surname, SchoolClass assignedClass) {
        super(name, surname, assignedClass);
    }
}
