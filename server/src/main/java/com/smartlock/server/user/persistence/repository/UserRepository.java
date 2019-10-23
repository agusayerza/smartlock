package com.smartlock.server.user.persistence.repository;

import com.smartlock.server.user.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//todo active true no more
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

//    todo testear
    User findFirstByEmailAndActiveTrue(String email);

    boolean existsByEmail(String email);

//    todo testear
    List<User> findAllByLocksIdContainingAndActiveTrue(Long lockId);

}
