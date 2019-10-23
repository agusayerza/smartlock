package com.smartlock.server.schedule.persistence.repository;

import com.smartlock.server.schedule.persistence.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    boolean existsByUserIdAndLockId(Long userId, Long lockId);

    List<Schedule> findAllByLockId(Long lockId);

//    todo falla aca
    List<Schedule> findAllByLockIdAndUserIdOrderByDayAsc(Long lockId, Long userId);
}
