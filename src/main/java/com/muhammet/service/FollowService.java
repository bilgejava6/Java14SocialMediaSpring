package com.muhammet.service;

import com.muhammet.config.JwtManager;
import com.muhammet.dto.request.AddFollowRequestDto;
import com.muhammet.entity.Follow;
import com.muhammet.exception.AuthException;
import com.muhammet.exception.ErrorType;
import com.muhammet.repository.FollowRepository;
import com.muhammet.utility.FollowState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository repository;
    private final JwtManager jwtManager;
    /**
     * Takip etme isteği
     * 1-> token takip etmek isteyen kişiyi belirtir ve içerisinde userId vardır
     * 2-> followId takip edilmek istenilen kişiyi belirtir.
     * ------
     * kullanıcı daha önce bu kişiyi takip etmek için istekte bulunmuş mu? takip isteği
     * hangi aşamada.
     * @param dto
     */
    public void addFollow(AddFollowRequestDto dto) {
        Long userId = getUserId(dto);
        Optional<Follow> optionalFollow = repository.findOptionalByUserIdAndFollowId(userId,dto.followId());
        if (optionalFollow.isEmpty()){
            add(userId,dto.followId());
        }else {
            switch (optionalFollow.get().getState()){
                case RED, TAKIP_ETMIYOR:
                    reFollowUser(optionalFollow);
            }
        }
    }
    public void unFollow(AddFollowRequestDto dto){
        Long userId = getUserId(dto);
        Optional<Follow> optionalFollow = repository.findOptionalByUserIdAndFollowId(userId,dto.followId());
        if (optionalFollow.isPresent()){
            Follow follow = optionalFollow.get();
            follow.setState(FollowState.TAKIP_ETMIYOR);
            repository.save(follow);
        }
    }

    private Long getUserId(AddFollowRequestDto dto) {
        Optional<Long> userId = jwtManager.getAuthId(dto.token());
        if(userId.isEmpty()) throw new AuthException(ErrorType.BAD_REQUEST_INVALID_TOKEN);
        return userId.get();
    }
    private void reFollowUser(Optional<Follow> optionalFollow) {
        Follow reFollow = optionalFollow.get();
        reFollow.setState(FollowState.BEKLEMEDE);
        repository.save(reFollow);
        return;
    }
    private void add(Long userId, Long followId){

        repository.save(Follow.builder()
                        .followId(followId)
                        .userId(userId)
                        .state(FollowState.BEKLEMEDE)
                .build());
    }
}
