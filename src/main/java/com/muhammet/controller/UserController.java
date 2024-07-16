package com.muhammet.controller;

import com.muhammet.dto.request.UserLoginRequestDto;
import com.muhammet.dto.request.UserSaveRequestDto;
import com.muhammet.dto.response.ResponseDto;
import com.muhammet.dto.response.SearchUserResponseDto;
import com.muhammet.entity.User;
import com.muhammet.exception.AuthException;
import com.muhammet.exception.ErrorType;
import com.muhammet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    @CrossOrigin("*")
    public ResponseEntity<User> save(@RequestBody UserSaveRequestDto dto){
        return ResponseEntity.ok(userService.save(dto));
    }

    @PostMapping("/login")
    @CrossOrigin("*")
    public ResponseEntity<ResponseDto<String>> login(@RequestBody UserLoginRequestDto dto){
        if(userService.login(dto).isEmpty())
            throw new AuthException(ErrorType.BAD_REQUEST_USERNAME_OR_PASSWORD_ERROR);
        return ResponseEntity.ok(ResponseDto.<String>builder()
                        .code(200)
                        .message("Başarılı şekilde giriş yapıldı")
                        .data("token")
                .build());
    }

    @GetMapping("/search")
    @CrossOrigin("*")
    public ResponseEntity<List<SearchUserResponseDto>> getUserList(String userName){
        return ResponseEntity.ok(userService.search(userName));
    }
}
