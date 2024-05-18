package com.web.springmvc.web_tin_tuc.dto;
import com.web.springmvc.web_tin_tuc.model.Category;
import com.web.springmvc.web_tin_tuc.model.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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
    @Size(min=10, max=85,message = "News title should not be empty (10->85 character)")
    private String title;
    private String thumbnail;
    @NotEmpty(message = "News content should not be empty")
    private String content;
    @Size(min=10, max=300,message = "News title should not be empty (10->150 character)")
    private String shortDescription;
    private Integer category;
    private String categoryName;
    private Boolean accepted = false;
    private User user;
    private String createdDate;
}
