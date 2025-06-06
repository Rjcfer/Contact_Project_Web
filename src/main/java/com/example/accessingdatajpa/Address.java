package com.example.accessingdatajpa;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
public class Address {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String street;
    private String postalCode;
    private String city;
    private String region;

    @ManyToMany(mappedBy = "addresses")
    private Collection<Contact> contacts;

    public Address () {}

    public Address(Long id, String street, String postalCode, String city, String region) {
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
        this.region = region;
    }

    public Long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getRegion() {
        return region;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
