package com.yo.yoprj.service;

import com.yo.yoprj.dto.user.UserResponse;
import com.yo.yoprj.dto.user.UserUpsertRequest;

import java.util.List;

public interface UserService {
    List<UserResponse> findAll();
    UserResponse findById(Integer id);
    UserResponse create(UserUpsertRequest req);
    UserResponse update(Integer id, UserUpsertRequest req);
    void deleteById(Integer id);
}
