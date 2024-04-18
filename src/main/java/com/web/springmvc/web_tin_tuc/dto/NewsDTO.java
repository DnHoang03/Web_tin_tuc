package com.web.springmvc.web_tin_tuc.dto;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Integer user;
    private LocalDateTime createdDate;
}
