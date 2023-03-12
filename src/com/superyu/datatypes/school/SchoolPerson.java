package com.superyu.datatypes.school;

import com.superyu.datatypes.common.Address;
import com.superyu.datatypes.common.Name;
import com.superyu.datatypes.common.PhoneNumber;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class SchoolPerson implements Comparable<SchoolPerson> {
    private final UUID id;
    private Name name;
    private Name surname;
    private HashSet<PhoneNumber> phoneNumbers;
    private HashSet<Address> addresses;
    private SchoolClass assignedClass;

    public SchoolPerson(Name name, Name surname, SchoolClass assignedClass) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.surname = surname;
        this.phoneNumbers = new HashSet<>();
        this.addresses = new HashSet<>();
        this.assignedClass = assignedClass;
    }

    public UUID getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Name getSurname() {
        return surname;
    }

    public void setSurname(Name surname) {
        this.surname = surname;
    }

    public SchoolPerson addPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumbers.add(phoneNumber);
        return this;
    }

    public SchoolPerson removePhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumbers.remove(phoneNumber);
        return this;
    }

    public SchoolPerson addAddress(Address address) {
        this.addresses.add(address);
        return this;
    }

    public SchoolPerson removeAddress(Address address) {
        this.addresses.remove(address);
        return this;
    }

    public void setPhoneNumbers(HashSet<PhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public void setAddresses(HashSet<Address> addresses) {
        this.addresses = addresses;
    }

    public HashSet<Address> getAddresses() {
        return addresses;
    }

    public HashSet<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public SchoolClass getAssignedClass() {
        return assignedClass;
    }

    public void setAssignedClass(SchoolClass assignedClass) {
        this.assignedClass = assignedClass;
    }

    @Override
    public String toString() {
        return name.toString() + " " + surname.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchoolPerson that = (SchoolPerson) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(surname, that.surname) && Objects.equals(phoneNumbers, that.phoneNumbers) && Objects.equals(addresses, that.addresses) && Objects.equals(assignedClass, that.assignedClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, phoneNumbers);
    }

    public void print() {
        System.out.println("Name: " + this.name.toString());
        System.out.println("Surname: " + this.surname.toString());

        System.out.println("Addresses:");
        for (Address currAddress : this.addresses) {
            System.out.println("\t" + currAddress.toString());
        }

        System.out.println("Phone Numbers:");
        for (PhoneNumber currPhoneNumber : this.phoneNumbers) {
            System.out.println("\t" + currPhoneNumber.toString());
        }

        System.out.println("Assigned Class: " + assignedClass.getId().toString());
        System.out.println("ID: " + id.toString());
    }

    @Override
    public int compareTo(SchoolPerson schoolPerson) {
        String combined1 = this.name.toString() + this.surname.toString();
        String combined2 = schoolPerson.getName().toString() + schoolPerson.getSurname().toString();
        return combined1.compareTo(combined2);
    }
}
