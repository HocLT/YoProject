package com.yo.yoprj.repository;

import com.yo.yoprj.domain.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    boolean existsByCourseClassIdAndStudentIdAndAttendanceDate(Integer courseClassId, Integer Integer, LocalDate attendanceDate);
    Optional<Attendance> findByCourseClassIdAndStudentIdAndAttendanceDate(Integer courseClassId, Integer studentId, LocalDate attendanceDate);

    List<Attendance> findByCourseClassId(Integer courseClassId);
    Page<Attendance> findByCourseClassId(Integer courseClassId, Pageable pageable);

    List<Attendance> findByStudentId(Integer studentId);
    Page<Attendance> findByStudentId(Integer studentId, Pageable pageable);

}
