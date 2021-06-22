package com.computershop.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.computershop.dao.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	User findByUsername(String username);
	
	List<User> findByUsernameContaining(String username);
	
	List<User> findByUsernameContainingIgnoreCase(String username);

}
