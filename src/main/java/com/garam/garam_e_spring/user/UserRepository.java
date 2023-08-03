package com.garam.garam_e_spring.user;

import com.garam.garam_e_spring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsUserByUserId(String userId);
    Optional<User> findByUserId(String userId);
}
