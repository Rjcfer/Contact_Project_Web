package com.example.accessingdatajpa;

import jakarta.persistence.*;

@Entity
public class EmailAddress {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String email;

    @ManyToOne
    @JoinColumn(name="fk_contact")
    private Contact contact;

    public EmailAddress() {}

    public EmailAddress(Long id, String email, Contact contact) {
        this.email = email;
        this.contact = contact;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Contact getContact() {
        return contact;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
