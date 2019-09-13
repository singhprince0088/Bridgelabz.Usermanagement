package com.bridgelabz.usermngmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.usermngmt.model.User;
import java.lang.String;
import java.util.List;
import java.util.Optional;

/**
 * Respository for performing operations in database
 * 
 * @since september-2019
 * @author Prince Singh
 * @version 1.0
 *
 */
@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	List<User> findByStatusTrue();

	List<User> findByStatusFalse();

}
