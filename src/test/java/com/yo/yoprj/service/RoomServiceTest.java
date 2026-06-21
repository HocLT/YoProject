package com.yo.yoprj.service;

import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.domain.entity.Room;
import com.yo.yoprj.dto.room.RoomResponse;
import com.yo.yoprj.dto.room.RoomUpsertRequest;
import com.yo.yoprj.repository.RoomRepository;
import com.yo.yoprj.service.impl.RoomServiceImpl;
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
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RoomServiceImpl roomService;

    @Test
    void findAllReturnsResponses() {
        Room room = new Room();
        room.setId(1);
        RoomResponse mockResponse = createMockResponse(1);

        when(roomRepository.findAll()).thenReturn(List.of(room));
        when(modelMapper.map(room, RoomResponse.class)).thenReturn(mockResponse);

        List<RoomResponse> result = roomService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(1);
    }

    @Test
    void findByIdSuccess() {
        Room room = new Room();
        room.setId(1);
        RoomResponse mockResponse = createMockResponse(1);

        when(roomRepository.findById(1)).thenReturn(Optional.of(room));
        when(modelMapper.map(room, RoomResponse.class)).thenReturn(mockResponse);

        RoomResponse result = roomService.findById(1);

        assertThat(result.id()).isEqualTo(1);
    }

    @Test
    void findByIdThrowsNotFound() {
        when(roomRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roomService.findById(1))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Room not found: 1");
    }

    @Test
    void createSuccess() {
        RoomUpsertRequest request = new RoomUpsertRequest("R01", "Room 1", 30, "Desc");
        RoomResponse mockResponse = createMockResponse(1);
        
        when(roomRepository.save(any(Room.class))).thenAnswer(invocation -> {
            Room r = invocation.getArgument(0);
            r.setId(1);
            return r;
        });
        
        doNothing().when(modelMapper).map(any(RoomUpsertRequest.class), any(Room.class));
        when(modelMapper.map(any(Room.class), eq(RoomResponse.class))).thenReturn(mockResponse);

        RoomResponse result = roomService.create(request);

        assertThat(result.id()).isEqualTo(1);
        verify(modelMapper).map(eq(request), any(Room.class));
    }

    @Test
    void updateSuccess() {
        RoomUpsertRequest request = new RoomUpsertRequest("R01", "Room 1", 30, "Desc");
        Room room = new Room();
        room.setId(1);
        RoomResponse mockResponse = createMockResponse(1);

        when(roomRepository.findById(1)).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(room);
        doNothing().when(modelMapper).map(any(RoomUpsertRequest.class), any(Room.class));
        when(modelMapper.map(any(Room.class), eq(RoomResponse.class))).thenReturn(mockResponse);

        RoomResponse result = roomService.update(1, request);

        assertThat(result.id()).isEqualTo(1);
        verify(modelMapper).map(eq(request), any(Room.class));
    }

    @Test
    void deleteByIdSuccess() {
        doNothing().when(roomRepository).deleteById(1);
        roomService.deleteById(1);
        verify(roomRepository).deleteById(1);
    }

    private RoomResponse createMockResponse(Integer id) {
        return new RoomResponse(id, "R01", "Room 1", 30, "Desc", null, null);
    }
}
