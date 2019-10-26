package com.smartlock.server.userValidator.persistence.repository;

import com.smartlock.server.userValidator.persistence.model.UserValidator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserValidatorRepository extends JpaRepository<UserValidator, String> {

    UserValidator findByEmailAndLockId(String email, Long lockId);
}
