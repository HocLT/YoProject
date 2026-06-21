package com.yo.yoprj.service;

import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.domain.entity.Parent;
import com.yo.yoprj.domain.entity.Student;
import com.yo.yoprj.domain.enums.Gender;
import com.yo.yoprj.domain.enums.Status;
import com.yo.yoprj.dto.student.StudentResponse;
import com.yo.yoprj.dto.student.StudentUpsertRequest;
import com.yo.yoprj.repository.ParentRepository;
import com.yo.yoprj.repository.StudentRepository;
import com.yo.yoprj.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ParentRepository parentRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    void findAllReturnsPagedResponses() {
        Student student = new Student();
        student.setId(1);
        Page<Student> page = new PageImpl<>(List.of(student));
        StudentResponse mockResponse = createMockResponse(1, "SV01");

        when(studentRepository.findAll(any(Pageable.class))).thenReturn(page);
        when(modelMapper.map(student, StudentResponse.class)).thenReturn(mockResponse);

        Page<StudentResponse> result = (Page<StudentResponse>) studentService.findAll(Pageable.unpaged());

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).id()).isEqualTo(1L);
    }

    @Test
    void findByIdSuccess() {
        Student student = new Student();
        student.setId(1);
        StudentResponse mockResponse = createMockResponse(1, "SV01");

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(modelMapper.map(student, StudentResponse.class)).thenReturn(mockResponse);

        StudentResponse result = studentService.findById(1);

        assertThat(result.id()).isEqualTo(1);
    }

    @Test
    void findByIdThrowsNotFound() {
        when(studentRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.findById(1))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Student not found: 1");
    }

    @Test
    void createSuccess() {
        StudentUpsertRequest request = new StudentUpsertRequest("SV01", "Name", LocalDate.now(), Gender.MALE, "10", "HS", "0123", 2, Status.ACTIVE, null, "Note");
        Parent parent = new Parent();
        parent.setId(2);

        when(parentRepository.findById(2)).thenReturn(Optional.of(parent));

        StudentResponse mockResponse = createMockResponse(1, "SV01");
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> {
            Student s = invocation.getArgument(0);
            s.setId(1);
            return s;
        });
        org.mockito.Mockito.doNothing().when(modelMapper).map(any(StudentUpsertRequest.class), any(Student.class));
        when(modelMapper.map(any(Student.class), eq(StudentResponse.class))).thenReturn(mockResponse);

        StudentResponse result = studentService.create(request);

        assertThat(result.id()).isEqualTo(1L);
        verify(modelMapper).map(eq(request), any(Student.class));
    }

    @Test
    void updateSuccess() {
        StudentUpsertRequest request = new StudentUpsertRequest("SV01", "Name", LocalDate.now(), Gender.MALE, "10", "HS", "0123", 2, Status.ACTIVE, null, "Note");
        Parent parent = new Parent();
        parent.setId(2);
        Student student = new Student();
        student.setId(1);

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(parentRepository.findById(2)).thenReturn(Optional.of(parent));

        StudentResponse mockResponse = createMockResponse(1, "SV01");
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        org.mockito.Mockito.doNothing().when(modelMapper).map(any(StudentUpsertRequest.class), any(Student.class));
        when(modelMapper.map(any(Student.class), eq(StudentResponse.class))).thenReturn(mockResponse);

        StudentResponse result = studentService.update(1, request);

        assertThat(result.id()).isEqualTo(1);
        verify(modelMapper).map(eq(request), any(Student.class));
    }

    @Test
    void createThrowsNotFoundWhenParentMissing() {
        StudentUpsertRequest request = new StudentUpsertRequest("SV01", "Name", LocalDate.now(), Gender.MALE, "10", "HS", "0123", 99, Status.ACTIVE, null, "Note");
        when(parentRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Parent not found: 99");
    }

    @Test
    void getStudentForParentSuccess() throws NotFoundException {
        Parent parent = new Parent();
        parent.setId(5);
        Student student = new Student();
        student.setId(1);
        student.setParent(parent);

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));

        Student result = studentService.getStudentForParent(1, 5);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
    }

    @Test
    void getStudentForParentThrowsAccessDenied() {
        Parent parent = new Parent();
        parent.setId(3);
        Student student = new Student();
        student.setId(1);
        student.setParent(parent);

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));

        assertThatThrownBy(() -> studentService.getStudentForParent(1, 5))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("Student does not belong to current parent account");
    }

    private StudentResponse createMockResponse(Integer id, String code) {
        return new StudentResponse(id, code, "Name", LocalDate.now(), "MALE", "10", "HS", "0123", 2, "Parent Name", "ACTIVE", null, "Note", null, null);
    }
}

