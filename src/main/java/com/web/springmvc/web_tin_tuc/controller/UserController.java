package com.web.springmvc.web_tin_tuc.controller;

import com.web.springmvc.web_tin_tuc.config.SecurityUtil;
import com.web.springmvc.web_tin_tuc.dto.UserDTO;
import com.web.springmvc.web_tin_tuc.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public String getUser(Model model) {
        UserDTO userDTO = new UserDTO();
        String username = SecurityUtil.getSessionUser();
        if(username == null) {
            return "redirect:/auth/login";
        }
        userDTO = userService.getUserByUsername(username);
        model.addAttribute("userEdit", userDTO);
        return "user-detail";
    }
    @PostMapping
    public String updateUser(@Valid @ModelAttribute("userEdit") UserDTO userDTO, BindingResult result) throws IOException {
        if(result.hasErrors()) return "user-detail";
        userService.updateUser(userDTO);
        return "redirect:/user?success";
    }
}
