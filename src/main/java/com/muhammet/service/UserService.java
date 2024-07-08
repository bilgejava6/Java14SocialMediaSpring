package com.muhammet.service;

import com.muhammet.dto.request.UserLoginRequestDto;
import com.muhammet.dto.request.UserSaveRequestDto;
import com.muhammet.dto.response.SearchUserResponseDto;
import com.muhammet.entity.User;
import com.muhammet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public User save(UserSaveRequestDto dto) {
        return repository.save(User.builder()
                        .password(dto.getPassword())
                        .email(dto.getEmail())
                        .userName(dto.getUserName())
                .build());
    }

    public Optional<User> login(UserLoginRequestDto dto) {
       return repository.findOptionalByUserNameAndPassword(dto.getUserName(),dto.getPassword());
    }

    public List<SearchUserResponseDto> search(String userName) {
        List<User> userList;
        List<SearchUserResponseDto> result = new ArrayList<>();
        if(Objects.isNull(userName))
            userList = repository.findAll();
        else
            userList = repository.findAllByUserNameContaining(userName);
        userList.forEach(u->
            result.add(SearchUserResponseDto.builder()
                            .userName(u.getUserName())
                            .avatar(u.getAvatar())
                            .email(u.getEmail())
                    .build())
        );
        return result;
    }
}
