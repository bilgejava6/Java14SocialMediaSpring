package com.muhammet.service;

import com.muhammet.config.JwtManager;
import com.muhammet.dto.request.FindAllByUsernameRequestDto;
import com.muhammet.dto.request.UserLoginRequestDto;
import com.muhammet.dto.request.UserProfileEditRequestDto;
import com.muhammet.dto.request.UserSaveRequestDto;
import com.muhammet.dto.response.SearchUserResponseDto;
import com.muhammet.entity.User;
import com.muhammet.exception.AuthException;
import com.muhammet.exception.ErrorType;
import com.muhammet.repository.UserRepository;
import com.muhammet.utility.BucketSubDirectoryName;
import com.muhammet.views.VwSearchUser;
import com.muhammet.views.VwUserAvatar;
import com.muhammet.views.VwUserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final JwtManager jwtManager;
    private final FollowService followService;
    private final MediaService mediaService;
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
                            .avatar(mediaService.getPhotoUrl(BucketSubDirectoryName.AVATAR,u.getAvatar()))
                            .email(u.getEmail())
                    .build())
        );
        return result;
    }

    public VwUserProfile getProfileByToken(String token) {
        Optional<Long> authId = jwtManager.getAuthId(token);
        if(authId.isEmpty()) throw new AuthException(ErrorType.BAD_REQUEST_INVALID_TOKEN);
        VwUserProfile result = repository.getByAuthId(authId.get());
        result.setAvatar(mediaService.getPhotoUrl(BucketSubDirectoryName.AVATAR,result.getAvatar()));
        return result;
    }

    public void editProfile(UserProfileEditRequestDto dto) {
        Optional<Long> authId = jwtManager.getAuthId(dto.token());
        if(authId.isEmpty()) throw new AuthException(ErrorType.BAD_REQUEST_INVALID_TOKEN);
        Optional<User> userOptional = repository.findById(authId.get());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setAbout(dto.about()!=null ? dto.about() : user.getAbout());
            user.setAddress(dto.address()!=null ? dto.address() : user.getAddress());
            user.setAvatar(dto.avatar()!=null ? dto.avatar() : user.getAvatar());
            user.setBornDate(dto.bornDate()!=null ? dto.bornDate() : user.getBornDate());
            user.setEmail(dto.email()!=null ? dto.email() : user.getEmail());
            user.setName(dto.name()!=null ? dto.name() : user.getName());
            repository.save(user);
        }
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
        Optional<Long> userId = jwtManager.getAuthId(dto.getToken());
        if(userId.isEmpty()) throw new AuthException(ErrorType.BAD_REQUEST_INVALID_TOKEN);
        List<Long> followIds = followService.findAllByUserId(userId.get());
        if (followIds.isEmpty()) followIds = List.of(0L);
        List<User> userList = repository
                .findAllByUserNameLikeAndIdNotIn("%"+dto.getUserName()+"%",followIds, PageRequest.of(0,6));
        return getVwSearchUsers(userList);
    }

    public List<VwSearchUser> getAllFollowList(List<Long> allFollowing) {
        List<User> userList = repository.findAllByIdIn(allFollowing, PageRequest.of(0,10));
        return getVwSearchUsers(userList);
    }

    private List<VwSearchUser> getVwSearchUsers(List<User> userList) {
        List<VwSearchUser> result = new ArrayList<>();
        userList.forEach(u->{
            result.add(
                    VwSearchUser.builder()
                            .avatar(mediaService.getPhotoUrl(BucketSubDirectoryName.AVATAR,u.getAvatar()))
                            .id(u.getId())
                            .name(u.getName())
                            .userName(u.getUserName())
                            .build()
            );
        });

        return result;
    }

}
