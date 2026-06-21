package com.yo.yoprj.service.impl;

import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.domain.entity.Room;
import com.yo.yoprj.dto.room.RoomResponse;
import com.yo.yoprj.dto.room.RoomUpsertRequest;
import com.yo.yoprj.repository.RoomRepository;
import com.yo.yoprj.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;

    private RoomResponse map(Room room) {
        return modelMapper.map(room, RoomResponse.class);
    }

    private Room toRoom(RoomUpsertRequest req) {
        Room room = new Room();
        modelMapper.map(req, room);
        return room;
    }

    @Override
    public List<RoomResponse> findAll() {
        return roomRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public RoomResponse findById(Integer id) {
        return roomRepository.findById(id)
                .map(this::map)
                .orElseThrow(() -> new NotFoundException("Room not found: " + id));
    }

    @Override
    public RoomResponse create(RoomUpsertRequest req) {
        Room room = toRoom(req);
        room = roomRepository.save(room);
        return map(room);
    }

    @Override
    public RoomResponse update(Integer id, RoomUpsertRequest req) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Room not found: " + id));
        modelMapper.map(req, room);
        room = roomRepository.save(room);
        return map(room);
    }

    @Override
    public void deleteById(Integer id) {
        roomRepository.deleteById(id);
    }
}
