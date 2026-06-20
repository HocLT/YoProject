package com.yo.yoprj.service.impl;

import com.yo.yoprj.domain.entity.Course;
import com.yo.yoprj.dto.course.CourseResponse;
import com.yo.yoprj.dto.course.CourseUpsertRequest;
import com.yo.yoprj.repository.CourseRepository;
import com.yo.yoprj.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final ModelMapper mapper;

    private Course toCourse(CourseUpsertRequest req) {
        return mapper.map(req, Course.class);
    }

    private CourseResponse toCourseResponse(Course course) {
        return mapper.map(course, CourseResponse.class);
    }

    public List<CourseResponse> findAll() {
        return courseRepository.findAll().stream()
                .map(this::toCourseResponse)
                .toList();
    }

    public Optional<CourseResponse> findById(Integer id) {
        return courseRepository.findById(id).map(this::toCourseResponse);
    }

    public CourseResponse create(CourseUpsertRequest req) {
        Course course = toCourse(req);
        Course result = courseRepository.save(course);
        return toCourseResponse(result);
    }

    public CourseResponse update(Integer id, CourseUpsertRequest req) {
        Course course = toCourse(req);
        course.setId(id);
        Course result = courseRepository.save(course);
        return toCourseResponse(result);
    }

    public void deleteById(Integer id) {
        courseRepository.deleteById(id);
    }
}
