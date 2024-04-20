package com.web.springmvc.web_tin_tuc.dto;
import com.web.springmvc.web_tin_tuc.model.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsDTO {
    private Integer id;
    @NotEmpty(message = "News title should not be empty")
    private String title;
    private String thumbnail;
    @NotEmpty(message = "News content should not be empty")
    private String content;
    private String shortDescription;
    private Integer category;
    private User user;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createdDate;
}
