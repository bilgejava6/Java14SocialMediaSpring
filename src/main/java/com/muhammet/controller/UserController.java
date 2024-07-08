package com.muhammet.controller;

import com.muhammet.dto.request.UserLoginRequestDto;
import com.muhammet.dto.request.UserSaveRequestDto;
import com.muhammet.entity.User;
import com.muhammet.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> login(@RequestBody UserLoginRequestDto dto){
        if(userService.login(dto).isEmpty())
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok("token");
    }
}
