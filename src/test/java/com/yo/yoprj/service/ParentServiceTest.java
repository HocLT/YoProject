package com.yo.yoprj.service;

import com.yo.yoprj.common.exception.NotFoundException;
import com.yo.yoprj.domain.entity.Parent;
import com.yo.yoprj.dto.parent.ParentResponse;
import com.yo.yoprj.dto.parent.ParentUpsertRequest;
import com.yo.yoprj.repository.ParentRepository;
import com.yo.yoprj.service.impl.ParentServiceImpl;
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
class ParentServiceTest {

    @Mock
    private ParentRepository parentRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ParentServiceImpl parentService;

    @Test
    void findAllReturnsResponses() {
        Parent parent = new Parent();
        parent.setId(1);
        ParentResponse mockResponse = createMockResponse(1);

        when(parentRepository.findAll()).thenReturn(List.of(parent));
        when(modelMapper.map(parent, ParentResponse.class)).thenReturn(mockResponse);

        List<ParentResponse> result = parentService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(1);
    }

    @Test
    void findByIdSuccess() {
        Parent parent = new Parent();
        parent.setId(1);
        ParentResponse mockResponse = createMockResponse(1);

        when(parentRepository.findById(1)).thenReturn(Optional.of(parent));
        when(modelMapper.map(parent, ParentResponse.class)).thenReturn(mockResponse);

        ParentResponse result = parentService.findById(1);

        assertThat(result.id()).isEqualTo(1);
    }

    @Test
    void findByIdThrowsNotFound() {
        when(parentRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> parentService.findById(1))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Parent not found: 1");
    }

    @Test
    void createSuccess() {
        ParentUpsertRequest request = new ParentUpsertRequest("Name", "0123", "email@test.com", "Address");
        ParentResponse mockResponse = createMockResponse(1);
        
        when(parentRepository.save(any(Parent.class))).thenAnswer(invocation -> {
            Parent p = invocation.getArgument(0);
            p.setId(1);
            return p;
        });
        
        doNothing().when(modelMapper).map(any(ParentUpsertRequest.class), any(Parent.class));
        when(modelMapper.map(any(Parent.class), eq(ParentResponse.class))).thenReturn(mockResponse);

        ParentResponse result = parentService.create(request);

        assertThat(result.id()).isEqualTo(1);
        verify(modelMapper).map(eq(request), any(Parent.class));
    }

    @Test
    void updateSuccess() {
        ParentUpsertRequest request = new ParentUpsertRequest("Name", "0123", "email@test.com", "Address");
        Parent parent = new Parent();
        parent.setId(1);
        ParentResponse mockResponse = createMockResponse(1);

        when(parentRepository.findById(1)).thenReturn(Optional.of(parent));
        when(parentRepository.save(any(Parent.class))).thenReturn(parent);
        doNothing().when(modelMapper).map(any(ParentUpsertRequest.class), any(Parent.class));
        when(modelMapper.map(any(Parent.class), eq(ParentResponse.class))).thenReturn(mockResponse);

        ParentResponse result = parentService.update(1, request);

        assertThat(result.id()).isEqualTo(1);
        verify(modelMapper).map(eq(request), any(Parent.class));
    }

    @Test
    void deleteByIdSuccess() {
        doNothing().when(parentRepository).deleteById(1);
        parentService.deleteById(1);
        verify(parentRepository).deleteById(1);
    }

    private ParentResponse createMockResponse(Integer id) {
        return new ParentResponse(id, "Name", "0123", "email@test.com", "Address", null, null);
    }
}
