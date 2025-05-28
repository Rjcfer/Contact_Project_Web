package com.example.accessingdatajpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserAccountRepository extends CrudRepository<UserAccount, Long>{

		List<UserAccount> findByUsername(String lastName);


	UserAccount findById(long id);

}
