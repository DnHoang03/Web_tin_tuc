package com.web.springmvc.web_tin_tuc.controller;

import com.web.springmvc.web_tin_tuc.config.SecurityUtil;
import com.web.springmvc.web_tin_tuc.dto.ChangePasswordRequest;
import com.web.springmvc.web_tin_tuc.dto.PasswordResetRequest;
import com.web.springmvc.web_tin_tuc.dto.UserDTO;
import com.web.springmvc.web_tin_tuc.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
        return "user/user-detail";
    }
    @PostMapping
    public String updateUser(@Valid @ModelAttribute("userEdit") UserDTO userDTO, BindingResult result) throws IOException {
        if(result.hasErrors()) return "user/user-detail";
        userService.updateUser(userDTO);
        return "redirect:/user?success";
    }

    @GetMapping("/change-password")
    public String changePasswordPage(Model model) {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        String username = SecurityUtil.getSessionUser();
        if(username == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("passwordChange", changePasswordRequest);
        return "user/password-change";
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute("passwordChange") ChangePasswordRequest request, BindingResult result) {
        if(result.hasErrors()) {
            return "user/password-change";
        }
        UserDTO user = userService.getUserByUsername(SecurityUtil.getSessionUser());
        System.out.println(user.getId());
        if(!userService.confirmOldPassword(request.getOldPassword(), user.getId())) {
            return "redirect:/user/change-password?failOld";
        }
        if(!request.getNewPassword().equals(request.getRepeatNewsPassword())) {
            return "redirect:/user/change-password?failNew";
        }
        if(request.getNewPassword().equals(request.getOldPassword())) {
            return "redirect:/user/change-password?failRepeat";
        }
        userService.changePassword(request, user.getId());
        return "redirect:/user?success";
    }
}
