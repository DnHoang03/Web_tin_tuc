//package com.web.springmvc.web_tin_tuc.service;
//
//import com.web.springmvc.newsweb.dto.AuthenticationRequest;
//import com.web.springmvc.newsweb.dto.AuthenticationResponse;
//import com.web.springmvc.newsweb.exception.UserNotFoundException;
//import com.web.springmvc.newsweb.model.User;
//import com.web.springmvc.newsweb.model.UserDetailsImpl;
//import com.web.springmvc.newsweb.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class AuthenticationService {
//    private final AuthenticationManager authenticationManager;
//    private final UserRepository userRepository;
//    private final JwtService jwtService;
//
//    public AuthenticationResponse authenticationRequest(AuthenticationRequest request) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
//        );
//        User user = new User();
//        user = userRepository.findByUsername(request.getUsername());
//        String token = jwtService.generateToken(new UserDetailsImpl(user));
//        return AuthenticationResponse.builder().token(token).build();
//    }
//}
