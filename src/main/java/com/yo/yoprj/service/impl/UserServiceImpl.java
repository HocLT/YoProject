package com.yo.yoprj.service.impl;

import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.domain.entity.User;
import com.yo.yoprj.dto.user.UserResponse;
import com.yo.yoprj.dto.user.UserUpsertRequest;
import com.yo.yoprj.repository.ParentRepository;
import com.yo.yoprj.repository.TeacherRepository;
import com.yo.yoprj.repository.UserRepository;
import com.yo.yoprj.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ParentRepository parentRepository;
    private final TeacherRepository teacherRepository;
    private final ModelMapper modelMapper;

    private UserResponse map(User user) {
        return modelMapper.map(user, UserResponse.class);
    }

    private User toUser(UserUpsertRequest req) {
        User user = new User();
        modelMapper.map(req, user);
        
        if (req.getParentId() != null && req.getParentId() > 0) {
            user.setParent(parentRepository.findById(req.getParentId())
                    .orElseThrow(() -> new NotFoundException("Parent not found: " + req.getParentId())));
        }
        
        if (req.getTeacherId() != null && req.getTeacherId() > 0) {
            user.setTeacher(teacherRepository.findById(req.getTeacherId())
                    .orElseThrow(() -> new NotFoundException("Teacher not found: " + req.getTeacherId())));
        }

        if (req.getIsActive() == null) {
            user.setIsActive(true);
        }
        
        return user;
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public UserResponse findById(Integer id) {
        return userRepository.findById(id)
                .map(this::map)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
    }

    @Override
    public UserResponse create(UserUpsertRequest req) {
        User user = toUser(req);
        user = userRepository.save(user);
        return map(user);
    }

    @Override
    public UserResponse update(Integer id, UserUpsertRequest req) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
        modelMapper.map(req, user);
        
        if (req.getParentId() != null && req.getParentId() > 0) {
            user.setParent(parentRepository.findById(req.getParentId())
                    .orElseThrow(() -> new NotFoundException("Parent not found: " + req.getParentId())));
        } else {
            user.setParent(null);
        }
        
        if (req.getTeacherId() != null && req.getTeacherId() > 0) {
            user.setTeacher(teacherRepository.findById(req.getTeacherId())
                    .orElseThrow(() -> new NotFoundException("Teacher not found: " + req.getTeacherId())));
        } else {
            user.setTeacher(null);
        }

        if (req.getIsActive() != null) {
            user.setIsActive(req.getIsActive());
        }

        user = userRepository.save(user);
        return map(user);
    }

    @Override
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }
}
