package com.muhammet.views;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VwSearchUser {
    Long id; // 8byte
    String userName; // 255byte
    String name; // 255byte
    String avatar; // 255byte
}
