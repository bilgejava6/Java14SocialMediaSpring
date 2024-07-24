package com.muhammet.dto.request;

public record UserProfileEditRequestDto(
        String email,
        String avatar,
        String name,
        String about,
        Integer bornDate,
        String phone,
        String address,
        String token
) {
}
