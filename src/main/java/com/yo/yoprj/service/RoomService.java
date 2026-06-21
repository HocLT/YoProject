package com.yo.yoprj.service;

import com.yo.yoprj.dto.room.RoomResponse;
import com.yo.yoprj.dto.room.RoomUpsertRequest;

import java.util.List;

public interface RoomService {
    List<RoomResponse> findAll();
    RoomResponse findById(Integer id);
    RoomResponse create(RoomUpsertRequest req);
    RoomResponse update(Integer id, RoomUpsertRequest req);
    void deleteById(Integer id);
}
