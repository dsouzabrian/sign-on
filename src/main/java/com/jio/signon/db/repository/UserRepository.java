package com.jio.signon.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;

import com.jio.signon.db.model.*;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

}
