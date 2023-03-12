package com.superyu.datatypes.common;

import com.superyu.datatypes.common.address.*;

import java.util.Objects;

public class Address {
    private final StreetName streetName;
    private final HouseNumber houseNumber;
    private final CityName cityName;
    private final ZipCode zipCode;
    private final CountryName countryName;

    public Address(StreetName streetName, HouseNumber houseNumber, CityName cityName, ZipCode zipCode, CountryName countryName) {
        this.streetName = streetName;
        this.houseNumber = houseNumber;
        this.cityName = cityName;
        this.zipCode = zipCode;
        this.countryName = countryName;
    }

    public StreetName getStreetName() {
        return streetName;
    }

    public HouseNumber getHouseNumber() {
        return houseNumber;
    }

    public CityName getCityName() {
        return cityName;
    }

    public ZipCode getZipCode() {
        return zipCode;
    }

    public CountryName getCountryName() {
        return countryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(streetName, address.streetName) && Objects.equals(houseNumber, address.houseNumber) && Objects.equals(cityName, address.cityName) && Objects.equals(zipCode, address.zipCode) && Objects.equals(countryName, address.countryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(streetName, houseNumber, cityName, zipCode, countryName);
    }

    @Override
    public String toString() {
        return streetName + " " + houseNumber + ", " + zipCode + ", " + cityName + ", " + countryName;
    }
}
