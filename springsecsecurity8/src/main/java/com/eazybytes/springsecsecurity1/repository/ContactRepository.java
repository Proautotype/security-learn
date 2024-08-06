package com.eazybytes.springsecsecurity1.repository;

import com.eazybytes.springsecsecurity1.model.Contact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ContactRepository extends CrudRepository<Contact, String> {
	
	
}
