package com.web.springmvc.web_tin_tuc.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    @Size(min = 6, max = 15, message = "Mật khẩu không hợp lệ! (6->15 ký tự)")
    private String oldPassword;
    @Size(min = 6, max = 15, message = "Mật khẩu không hợp lệ! (6->15 ký tự)")
    private String newPassword;
    @Size(min = 6, max = 15, message = "Mật khẩu không hợp lệ! (6->15 ký tự)")
    private String repeatNewsPassword;
    private Integer userId;
}
