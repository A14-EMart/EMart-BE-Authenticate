package com.a14.emart.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class Manager {
    private Long id;
    private String username;

}
