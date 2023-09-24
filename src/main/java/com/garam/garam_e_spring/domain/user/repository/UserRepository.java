package com.garam.garam_e_spring.domain.user.repository;

import com.garam.garam_e_spring.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsUserByUserId(String userId);
    Optional<User> findByUserId(String userId);
    void deleteByUserId(String userId);
}
