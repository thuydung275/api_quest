package com.quest.etna.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.quest.etna.model.User;
import com.quest.etna.model.User.UserRole;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
	
	@Query("SELECT u FROM User u WHERE u.username = :username")
	public Optional<User> findByUsername(String username);
	
	@Query("SELECT u FROM User u WHERE u.username LIKE %:username%")
	public List<User> findAllByUsername(String username);
	
	@Query("SELECT u FROM User u WHERE u.userRole = :role")
	public List<User> findAllByUserRole(UserRole role);
	
	@Query("SELECT u FROM User u ORDER BY u.id ASC")
	public List<User> getListPerPage(Pageable pageable);
}
