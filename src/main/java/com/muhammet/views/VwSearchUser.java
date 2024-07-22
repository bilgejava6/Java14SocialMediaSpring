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
    Long id;
    String userName;
    String name;
    String avatar;
}
