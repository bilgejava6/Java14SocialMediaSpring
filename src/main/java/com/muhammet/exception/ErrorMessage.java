package com.muhammet.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorMessage {
    private Integer code;
    private String message;
    /**
     * String password: @Valid, min=8, max=32, Enaz  bir Büyük Harf, Bir Küçük Harf, Özel karakter ....
     *
     */
    private List<String> fields;
}
