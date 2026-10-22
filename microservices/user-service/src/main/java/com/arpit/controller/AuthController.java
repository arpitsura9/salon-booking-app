package com.arpit.controller;


import com.arpit.payload.dto.LoginDTO;
import com.arpit.payload.dto.SignupDTO;
import com.arpit.payload.response.AuthResponse;
import com.arpit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(
            @RequestBody SignupDTO req
    ) throws Exception{
        AuthResponse res = authService.signup(req);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginDTO req
    ) throws Exception{

        AuthResponse res = authService.login(req.getEmail(),req.getPassword());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/access-token/refresh-token/{refresh-token}")
    public ResponseEntity<AuthResponse> getAccessToken(
            @PathVariable("refresh-token") String refreshToken
    ) throws Exception{

        AuthResponse res = authService.getAccessTokenFromRefreshToken(refreshToken);
        return ResponseEntity.ok(res);
    }
}
