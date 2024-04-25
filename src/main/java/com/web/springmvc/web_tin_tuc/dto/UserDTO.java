package com.web.springmvc.web_tin_tuc.dto;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Integer id;
    @NotEmpty
    private String email;
    private String username;
    private String password;
    private String role;
    private String firstName;
    private String lastName;
    private String photoUrl;
    public UserDTO(String email, String username, String password, String role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
