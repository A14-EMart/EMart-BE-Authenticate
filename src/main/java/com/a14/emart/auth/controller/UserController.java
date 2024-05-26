package com.a14.emart.auth.controller;

import com.a14.emart.auth.dto.GetManagerResponse;
import com.a14.emart.auth.dto.Manager;
import com.a14.emart.auth.service.JwtService;
import com.a14.emart.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @GetMapping("/managers")
    public ResponseEntity<GetManagerResponse> getManagers(@RequestHeader("Authorization") String token) {
        try {
            String tokenWithoutBearer = token.replace("Bearer ", "");
            String role = jwtService.extractRole(tokenWithoutBearer);
            System.out.println(role);

            if (!role.equalsIgnoreCase("admin")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new GetManagerResponse(null));
            }

            List<Manager> managers = userService.getManagers();
            GetManagerResponse response = new GetManagerResponse(managers);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GetManagerResponse(null));
        }
    }
}
