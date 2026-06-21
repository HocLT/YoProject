package com.yo.yoprj.repository;

import com.yo.yoprj.domain.entity.CourseClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseClassRepository extends JpaRepository<CourseClass, Integer> {
}
