package com.superyu.datatypes.school;

import com.superyu.datatypes.common.Name;

public class Teacher extends SchoolPerson {
    public Teacher(Name name, Name surname, SchoolClass assignedClass) {
        super(name, surname, assignedClass);
    }
}
