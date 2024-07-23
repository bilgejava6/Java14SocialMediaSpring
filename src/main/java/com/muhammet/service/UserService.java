package com.muhammet.service;

import com.muhammet.config.JwtManager;
import com.muhammet.dto.request.FindAllByUsernameRequestDto;
import com.muhammet.dto.request.UserLoginRequestDto;
import com.muhammet.dto.request.UserSaveRequestDto;
import com.muhammet.dto.response.SearchUserResponseDto;
import com.muhammet.entity.User;
import com.muhammet.exception.AuthException;
import com.muhammet.exception.ErrorType;
import com.muhammet.repository.UserRepository;
import com.muhammet.views.VwSearchUser;
import com.muhammet.views.VwUserAvatar;
import com.muhammet.views.VwUserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final JwtManager jwtManager;
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

    public VwUserProfile getProfileByToken(String token) {
        Optional<Long> authId = jwtManager.getAuthId(token);
        if(authId.isEmpty()) throw new AuthException(ErrorType.BAD_REQUEST_INVALID_TOKEN);
        return repository.getByAuthId(authId.get());
    }

    public void editProfile(User user) {
        repository.save(user);
    }

    public VwUserAvatar getUserAvatar(Long id){
        return repository.getUserAvatar(id);
    }

    public List<VwUserAvatar> getUserAvatarList(){
        return repository.getUserAvatarList();
    }

    public List<User> findAllByIds(List<Long> userIds){
        return repository.findAllById(userIds);
    }

    public Map<Long,User> findAllByIdsMap(List<Long> userIds){
        List<User> userList = repository.findAllById(userIds);
        Map<Long,User> result = userList.stream().collect(
                Collectors.toMap(User::getId,u-> u)
        );
        return result;
    }

    public List<VwSearchUser> getAllByUserName(FindAllByUsernameRequestDto dto) {
        Optional<Long> authId = jwtManager.getAuthId(dto.getToken());
        if(authId.isEmpty()) throw new AuthException(ErrorType.BAD_REQUEST_INVALID_TOKEN);
        return repository.getAllByUserName("%"+dto.getUserName()+"%");
    }


}
