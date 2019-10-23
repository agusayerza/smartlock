package com.smartlock.server.lock.persistence.repository;

import com.smartlock.server.lock.persistence.model.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LockRepository extends JpaRepository<Lock, Long> {

}
