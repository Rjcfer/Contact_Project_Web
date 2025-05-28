package com.example.accessingdatajpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ContactRepository extends CrudRepository<Contact, Long> {

    List<Contact> findByLastName(String lastName);

    Contact findById(long id);
}