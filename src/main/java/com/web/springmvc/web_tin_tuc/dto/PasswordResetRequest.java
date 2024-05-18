package com.web.springmvc.web_tin_tuc.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetRequest {
    @Size(min = 6, max = 15, message = "Mật khẩu không hợp lệ! (6->15 ký tự)")
    private String password;
    @Size(min = 6, max = 15, message = "Mật khẩu không hợp lệ! (6->15 ký tự)")
    private String repeatPassword;
    private Integer userId;
}
