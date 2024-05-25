package com.a14.emart.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class GetManagerResponse {
    List<Manager> managers;

}
