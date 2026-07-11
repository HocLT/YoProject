package com.yo.yoprj.repository;

import com.yo.yoprj.domain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {

//    @Query("SELECT o FROM Student o WHERE o.studentCode LIKE :code")
//    public List<Student> findByStudentCodeLike(@Param("code") String code);

    public List<Student> findByStudentCodeLike(String code);

    java.util.List<Student> findByParentId(Integer parentId);
}
