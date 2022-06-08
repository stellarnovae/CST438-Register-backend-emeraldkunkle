package com.cst438.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.RequestParam;

public interface AdminRepository extends CrudRepository <Admin, Integer> {
	
	
	// declare the following method to return a single Student object
	// default JPA behavior that findBy methods return List<Student> except for findById.
	public Admin findByEmail(String email);

}
