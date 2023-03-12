package com.superyu.managers;

import com.superyu.datatypes.common.Name;
import com.superyu.datatypes.school.SchoolClass;
import com.superyu.datatypes.school.SchoolGrade;
import com.superyu.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

public class SchoolClassManager {
    HashSet<SchoolClass> schoolClasses;

    public SchoolClassManager() {
        this.schoolClasses = new HashSet<>();
    }

    public ArrayList<SchoolClass> sortedArrayList() {
        ArrayList<SchoolClass> arrayList = new ArrayList<>(this.schoolClasses);
        Collections.sort(arrayList);
        return arrayList;
    }

    public SchoolClassManager addSchoolClass(SchoolClass schoolClass) {
        this.schoolClasses.add(schoolClass);
        return this;
    }

    public SchoolClassManager removeSchoolClass(SchoolClass schoolClass) {
        this.schoolClasses.remove(schoolClass);
        return this;
    }

    public SchoolClass getClassByGrade(SchoolGrade grade) throws NotFoundException {
        for (SchoolClass currSchoolClass : this.schoolClasses) {
            if (currSchoolClass.getSchoolGrade().equals(grade))
                return currSchoolClass;
        }
        throw new NotFoundException("No class with that grade found!");
    }

    public SchoolClass getClassByName(Name name) throws NotFoundException {
        for (SchoolClass currSchoolClass : this.schoolClasses) {
            if (currSchoolClass.getClassName().equals(name))
                return currSchoolClass;
        }
        throw new NotFoundException("No class with that name found!");
    }

    public SchoolClass getClassByID(UUID id) throws NotFoundException {
        for (SchoolClass currSchoolClass : this.schoolClasses) {
            if (currSchoolClass.getId().equals(id))
                return currSchoolClass;
        }
        throw new NotFoundException("No class with that id found!");
    }
}
