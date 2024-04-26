package com.web.springmvc.web_tin_tuc.dto;

import com.web.springmvc.web_tin_tuc.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDTO {
    private Integer id;
    private Integer newsId;
    private String content;
    private UserDTO userDTO;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdDate;
}
