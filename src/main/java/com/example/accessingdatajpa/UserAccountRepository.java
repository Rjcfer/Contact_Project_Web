package com.example.accessingdatajpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserAccountRepository extends CrudRepository<UserAccount, Long>{

	UserAccount findByUsername(String username);

	UserAccount findById(long id);
}
