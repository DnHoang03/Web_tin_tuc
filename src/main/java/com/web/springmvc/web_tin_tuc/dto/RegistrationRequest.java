package com.web.springmvc.web_tin_tuc.dto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    @NotEmpty(message = "Email must not be empty")
    private String email;
    @Size(min = 6, max = 15, message = "Invalid username! (6->15 character)")
    private String username;
    @Size(min = 6, max = 15, message = "Invalid password! (6->15 character)")
    private String password;
    @Size(min = 6, max = 15, message = "Invalid password! (6->15 character)")
    private String repeatPassword;
}
