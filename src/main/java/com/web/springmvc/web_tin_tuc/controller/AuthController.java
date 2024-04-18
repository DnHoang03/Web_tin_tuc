//package com.web.springmvc.web_tin_tuc.controller;
//
//import com.web.springmvc.newsweb.dto.AuthenticationRequest;
//import com.web.springmvc.newsweb.dto.AuthenticationResponse;
//import com.web.springmvc.newsweb.dto.UserDTO;
//import com.web.springmvc.newsweb.model.User;
//import com.web.springmvc.newsweb.service.AuthenticationService;
//import com.web.springmvc.newsweb.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//@Controller
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final AuthenticationService authenticationService;
//    private final UserService userService;
//
//    @GetMapping("/login")
//    public String loginPage() {
//        return "login";
//    }
//
//    @PostMapping("/register")
//    public ResponseEntity<UserDTO> register(@RequestBody UserDTO request) {
//        return ResponseEntity.ok(userService.register(request));
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
////        System.out.println(request);
//        return ResponseEntity.ok(authenticationService.authenticationRequest(request));
//    }
//}
