package com.smartlock.server.user.persistence.repository;

import com.smartlock.server.user.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findFirstByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findAllByLocksIdContaining(Long lockId);

    Optional<User> findByEmail(String email);
}
