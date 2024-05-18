package com.web.springmvc.web_tin_tuc.controller;

import com.web.springmvc.web_tin_tuc.dto.ForgotPasswordRequest;
import com.web.springmvc.web_tin_tuc.dto.RegistrationRequest;
import com.web.springmvc.web_tin_tuc.dto.PasswordResetRequest;
import com.web.springmvc.web_tin_tuc.dto.UserDTO;
import com.web.springmvc.web_tin_tuc.service.RegistrationService;
import com.web.springmvc.web_tin_tuc.service.PasswordResetService;
import com.web.springmvc.web_tin_tuc.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    private final PasswordResetService passwordResetService;
    @GetMapping("/register")
    public String registerPage(Model model) {
        RegistrationRequest request = new RegistrationRequest();
        model.addAttribute("request", request);
        return "auth/register";
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
            return "auth/register";
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
            return "auth/register";
        }
        registrationService.register(request);
        return "redirect:/auth/register?authen";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage(Model model) {
        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        model.addAttribute("emailDTO", forgotPasswordRequest);
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@Valid @ModelAttribute("emailDTO") ForgotPasswordRequest forgotPasswordRequest, BindingResult result) {
        if(result.hasErrors()) {
            return "auth/forgot-password";
        }
        UserDTO user = userService.getUserByEmail(forgotPasswordRequest.getEmail());
        if(user == null) {
            return "redirect:/auth/forgot-password?error";
        }
        passwordResetService.sendPasswordReset(forgotPasswordRequest, user.getId());
        return "redirect:/auth/forgot-password?success";
    }

    @GetMapping("/reset-password/{id}")
    public String resetPasswordPage(@RequestParam("token") String token, @PathVariable("id") Integer id, Model model) {
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setUserId(id);
        model.addAttribute("resetPassword", passwordResetRequest);
        passwordResetService.confirmToken(token);
        return "auth/password-reset";
    }

    @PostMapping("/reset-password/{id}")
    public String resetPassword(@Valid @ModelAttribute("resetPassword") PasswordResetRequest passwordResetRequest, BindingResult result, Model model, @PathVariable("id") Integer id) {
        if(result.hasErrors()) {
            return "auth/password-reset";
        }
        if(!passwordResetRequest.getPassword().equals(passwordResetRequest.getRepeatPassword())) {
            model.addAttribute("passwordError", "Password and Repeat Password do not match");
            return "auth/password-reset";
        }
        passwordResetService.resetPassword(id, passwordResetRequest);
        return "redirect:/auth/login?rs_success";
    }
}
