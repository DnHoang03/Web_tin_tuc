package com.web.springmvc.web_tin_tuc.controller;

import com.web.springmvc.web_tin_tuc.dto.UserDTO;
import com.web.springmvc.web_tin_tuc.model.User;
import com.web.springmvc.web_tin_tuc.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/register")
    public String registerPage(Model model) {
        UserDTO userDTO = new UserDTO();
        model.addAttribute("user", userDTO);
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult result) {
        if(result.hasErrors()) {
            return "register";
        }
        User user = userService.getUserByEmail(userDTO.getEmail());
        if(user != null && user.getStatus() != 0) {
            return "redirect:/api/auth/register?fail";
        }
        User userr = userService.getUserByUsername(userDTO.getUsername());
        if(userr != null && userr.getStatus() != 0) {
            return "redirect:/api/auth/register?fail";
        }
        userService.register(userDTO);
        return "redirect:/api/news?success";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "login";
        }
        return "redirect:/api/news";
    }

//    @PostMapping("/login")
//    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
////        System.out.println(request);
//        return ResponseEntity.ok(authenticationService.authenticationRequest(request));
//    }
}
