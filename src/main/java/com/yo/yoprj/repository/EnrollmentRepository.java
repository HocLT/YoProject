package com.yo.yoprj.repository;

import com.yo.yoprj.domain.entity.Enrollment;
import com.yo.yoprj.domain.enums.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Integer> {

    boolean existsByStudentIdAndCourseClassId(Integer studentId, Integer courseClassId);

    int countByCourseClassIdAndStatus(Integer courseClassId, EnrollmentStatus status);

    List<Enrollment> findByCourseClassId(Integer courseClassId);
    Page<Enrollment> findByCourseClassId(Integer courseClassId, Pageable pageable);

    Optional<Enrollment> findByStudentIdAndCourseClassId(Integer studentId, Integer courseClassId);

    List<Enrollment> findByStudentId(Integer studentId);
    Page<Enrollment> findByStudentId(Integer studentId, Pageable pageable);
}
