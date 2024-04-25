package com.web.springmvc.web_tin_tuc.controller;

import com.web.springmvc.web_tin_tuc.dto.RegistrationRequest;
import com.web.springmvc.web_tin_tuc.dto.UserDTO;
import com.web.springmvc.web_tin_tuc.model.User;
import com.web.springmvc.web_tin_tuc.service.RegistrationService;
import com.web.springmvc.web_tin_tuc.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final RegistrationService registrationService;
    @GetMapping("/register")
    public String registerPage(Model model) {
        RegistrationRequest request = new RegistrationRequest();
        model.addAttribute("request", request);
        return "register";
    }

    @GetMapping("/register/confirm")
    public String registerConfirm(@RequestParam(value = "token") String token, Model model) {
        RegistrationRequest request = new RegistrationRequest();
        registrationService.confirmToken(token);
        model.addAttribute("request", request);
        return "redirect:/auth/login?authenSuccess";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("request") RegistrationRequest request, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "register";
        }
        UserDTO user = userService.getUserByEmail(request.getEmail());
        if(user != null) {
            return "redirect:/auth/register?fail";
        }
        UserDTO userr = userService.getUserByUsername(request.getUsername());
        if(userr != null) {
            return "redirect:/auth/register?fail";
        }
        if(!request.getPassword().equals(request.getRepeatPassword())) {
            model.addAttribute("passwordError", "Password and Repeat Password do not match");
            return "register";
        }
        registrationService.register(request);
        return "redirect:/auth/register?authen";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }


}
