package com.yo.yoprj.service.impl;

import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.domain.entity.Teacher;
import com.yo.yoprj.dto.teacher.TeacherResponse;
import com.yo.yoprj.dto.teacher.TeacherUpsertRequest;
import com.yo.yoprj.repository.TeacherRepository;
import com.yo.yoprj.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;

    private TeacherResponse map(Teacher teacher) {
        return modelMapper.map(teacher, TeacherResponse.class);
    }

    private Teacher toTeacher(TeacherUpsertRequest req) {
        Teacher teacher = new Teacher();
        modelMapper.map(req, teacher);
        if (req.getIsActive() == null) {
            teacher.setIsActive(true);
        }
        return teacher;
    }

    @Override
    public List<TeacherResponse> findAll() {
        return teacherRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public TeacherResponse findById(Integer id) {
        return teacherRepository.findById(id)
                .map(this::map)
                .orElseThrow(() -> new NotFoundException("Teacher not found: " + id));
    }

    @Override
    public TeacherResponse create(TeacherUpsertRequest req) {
        Teacher teacher = toTeacher(req);
        teacher = teacherRepository.save(teacher);
        return map(teacher);
    }

    @Override
    public TeacherResponse update(Integer id, TeacherUpsertRequest req) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Teacher not found: " + id));
        modelMapper.map(req, teacher);
        if (req.getIsActive() != null) {
            teacher.setIsActive(req.getIsActive());
        }
        teacher = teacherRepository.save(teacher);
        return map(teacher);
    }

    @Override
    public void deleteById(Integer id) {
        teacherRepository.deleteById(id);
    }
}
