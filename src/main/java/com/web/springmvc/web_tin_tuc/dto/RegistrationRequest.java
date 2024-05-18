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
    @NotEmpty(message = "Email không được bỏ trống")
    private String email;
    @Size(min = 6, max = 15, message = "Mật khẩu không hợp lệ! (6->15 ký tự)")
    private String username;
    @Size(min = 6, max = 15, message = "Mật khẩu không hợp lệ! (6->15 ký tự)")
    private String password;
    @Size(min = 6, max = 15, message = "Mật khẩu không hợp lệ! (6->15 ký tự)")
    private String repeatPassword;
    @NotEmpty(message = "Không được bỏ trống")
    private String firstName;
    @NotEmpty(message = "Không được bỏ trống")
    private String lastName;
}
