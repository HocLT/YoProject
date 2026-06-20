package com.yo.yoprj.repository;

import com.yo.yoprj.domain.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
