package com.yo.yoprj.repository;

import com.yo.yoprj.domain.entity.CourseClass;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CourseClassRepository extends JpaRepository<CourseClass, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM CourseClass c WHERE c.id = :id")
    Optional<CourseClass> findByIdWithLock(@Param("id") Integer id);
}
