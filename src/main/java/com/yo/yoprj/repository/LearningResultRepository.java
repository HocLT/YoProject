package com.yo.yoprj.repository;

import com.yo.yoprj.domain.entity.LearningResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LearningResultRepository extends JpaRepository<LearningResult, Integer> {

    boolean existsByStudentIdAndCourseClassIdAndResultMonth(Integer studentId, Integer courseClassId, java.time.LocalDate resultMonth);

    java.util.List<LearningResult> findByStudentId(Integer studentId);
}
