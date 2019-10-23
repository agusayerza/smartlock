package com.smartlock.server.user.persistence.repository;

import com.smartlock.server.user.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

//    todo testear
    User findFirstByEmail(String email);

    boolean existsByEmail(String email);

//    todo testear
    List<User> findAllByLocksIdContaining(Long lockId);

}
