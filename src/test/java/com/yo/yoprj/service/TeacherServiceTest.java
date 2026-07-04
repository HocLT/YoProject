package com.yo.yoprj.service;

import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.domain.entity.Teacher;
import com.yo.yoprj.domain.enums.TeacherRole;
import com.yo.yoprj.dto.teacher.TeacherResponse;
import com.yo.yoprj.dto.teacher.TeacherUpsertRequest;
import com.yo.yoprj.repository.TeacherRepository;
import com.yo.yoprj.service.impl.TeacherServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TeacherServiceImpl teacherService;

    @Test
    void findAllReturnsResponses() {
        Teacher teacher = new Teacher();
        teacher.setId(1);
        TeacherResponse mockResponse = createMockResponse(1);

        when(teacherRepository.findAll()).thenReturn(List.of(teacher));
        when(modelMapper.map(teacher, TeacherResponse.class)).thenReturn(mockResponse);

        List<TeacherResponse> result = teacherService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1);
    }

    @Test
    void findByIdSuccess() {
        Teacher teacher = new Teacher();
        teacher.setId(1);
        TeacherResponse mockResponse = createMockResponse(1);

        when(teacherRepository.findById(1)).thenReturn(Optional.of(teacher));
        when(modelMapper.map(teacher, TeacherResponse.class)).thenReturn(mockResponse);

        TeacherResponse result = teacherService.findById(1);

        assertThat(result.getId()).isEqualTo(1);
    }

    @Test
    void findByIdThrowsNotFound() {
        when(teacherRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> teacherService.findById(1))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Teacher not found: 1");
    }

    @Test
    void createSuccess() {
        TeacherUpsertRequest request = new TeacherUpsertRequest("T01", "Name", "0123", "email", TeacherRole.TEACHER, "url", true);
        TeacherResponse mockResponse = createMockResponse(1);
        
        when(teacherRepository.save(any(Teacher.class))).thenAnswer(invocation -> {
            Teacher t = invocation.getArgument(0);
            t.setId(1);
            return t;
        });
        
        doNothing().when(modelMapper).map(any(TeacherUpsertRequest.class), any(Teacher.class));
        when(modelMapper.map(any(Teacher.class), eq(TeacherResponse.class))).thenReturn(mockResponse);

        TeacherResponse result = teacherService.create(request);

        assertThat(result.getId()).isEqualTo(1);
        verify(modelMapper).map(eq(request), any(Teacher.class));
    }

    @Test
    void updateSuccess() {
        TeacherUpsertRequest request = new TeacherUpsertRequest("T01", "Name", "0123", "email", TeacherRole.TEACHER, "url", true);
        Teacher teacher = new Teacher();
        teacher.setId(1);
        TeacherResponse mockResponse = createMockResponse(1);

        when(teacherRepository.findById(1)).thenReturn(Optional.of(teacher));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacher);
        doNothing().when(modelMapper).map(any(TeacherUpsertRequest.class), any(Teacher.class));
        when(modelMapper.map(any(Teacher.class), eq(TeacherResponse.class))).thenReturn(mockResponse);

        TeacherResponse result = teacherService.update(1, request);

        assertThat(result.getId()).isEqualTo(1);
        verify(modelMapper).map(eq(request), any(Teacher.class));
    }

    @Test
    void deleteByIdSuccess() {
        doNothing().when(teacherRepository).deleteById(1);
        teacherService.deleteById(1);
        verify(teacherRepository).deleteById(1);
    }

    private TeacherResponse createMockResponse(Integer id) {
        return new TeacherResponse(id, "T01", "Name", "0123", "email", "GIANG_VIEN", "url", true, null, null);
    }
}
