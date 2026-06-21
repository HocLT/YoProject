package com.yo.yoprj.service;

import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.domain.entity.Parent;
import com.yo.yoprj.domain.entity.Teacher;
import com.yo.yoprj.domain.entity.User;
import com.yo.yoprj.domain.enums.UserRole;
import com.yo.yoprj.dto.user.UserResponse;
import com.yo.yoprj.dto.user.UserUpsertRequest;
import com.yo.yoprj.repository.ParentRepository;
import com.yo.yoprj.repository.TeacherRepository;
import com.yo.yoprj.repository.UserRepository;
import com.yo.yoprj.service.impl.UserServiceImpl;
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
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ParentRepository parentRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findAllReturnsResponses() {
        User user = new User();
        user.setId(1);
        UserResponse mockResponse = createMockResponse(1);

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(modelMapper.map(user, UserResponse.class)).thenReturn(mockResponse);

        List<UserResponse> result = userService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(1);
    }

    @Test
    void findByIdSuccess() {
        User user = new User();
        user.setId(1);
        UserResponse mockResponse = createMockResponse(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserResponse.class)).thenReturn(mockResponse);

        UserResponse result = userService.findById(1);

        assertThat(result.id()).isEqualTo(1);
    }

    @Test
    void findByIdThrowsNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(1))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User not found: 1");
    }

    @Test
    void createSuccess() {
        UserUpsertRequest request = new UserUpsertRequest("user", "hash", "Name", "0123", "email", UserRole.PARENT, 2, null, true);
        Parent parent = new Parent();
        parent.setId(2);
        UserResponse mockResponse = createMockResponse(1);
        
        when(parentRepository.findById(2)).thenReturn(Optional.of(parent));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1);
            return u;
        });
        
        doNothing().when(modelMapper).map(any(UserUpsertRequest.class), any(User.class));
        when(modelMapper.map(any(User.class), eq(UserResponse.class))).thenReturn(mockResponse);

        UserResponse result = userService.create(request);

        assertThat(result.id()).isEqualTo(1);
        verify(modelMapper).map(eq(request), any(User.class));
    }

    @Test
    void updateSuccess() {
        UserUpsertRequest request = new UserUpsertRequest("user", "hash", "Name", "0123", "email", UserRole.ADMIN, null, 3, true);
        Teacher teacher = new Teacher();
        teacher.setId(3);
        User user = new User();
        user.setId(1);
        UserResponse mockResponse = createMockResponse(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(teacherRepository.findById(3)).thenReturn(Optional.of(teacher));
        when(userRepository.save(any(User.class))).thenReturn(user);
        
        doNothing().when(modelMapper).map(any(UserUpsertRequest.class), any(User.class));
        when(modelMapper.map(any(User.class), eq(UserResponse.class))).thenReturn(mockResponse);

        UserResponse result = userService.update(1, request);

        assertThat(result.id()).isEqualTo(1);
        verify(modelMapper).map(eq(request), any(User.class));
    }

    @Test
    void deleteByIdSuccess() {
        doNothing().when(userRepository).deleteById(1);
        userService.deleteById(1);
        verify(userRepository).deleteById(1);
    }

    private UserResponse createMockResponse(Integer id) {
        return new UserResponse(id, "user", "Name", "0123", "email", "ROLE_PHU_HUYNH", 2, null, true, null, null);
    }
}
