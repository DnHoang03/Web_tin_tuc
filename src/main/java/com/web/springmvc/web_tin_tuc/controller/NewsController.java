package com.web.springmvc.web_tin_tuc.controller;

import com.web.springmvc.web_tin_tuc.config.SecurityUtil;
import com.web.springmvc.web_tin_tuc.dto.*;
import com.web.springmvc.web_tin_tuc.model.User;
import com.web.springmvc.web_tin_tuc.service.CategoryService;
import com.web.springmvc.web_tin_tuc.service.CommentService;
import com.web.springmvc.web_tin_tuc.service.NewsService;
import com.web.springmvc.web_tin_tuc.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final CommentService commentService;
    @GetMapping
    public String getAllNews(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber
            ,@RequestParam(value = "pageSize", defaultValue = "6", required = false)int pageSize
            ,Model model) {
        UserDTO user = new UserDTO();
        String username = SecurityUtil.getSessionUser();
        if(username != null) {
            user = userService.getUserByUsername(username);
        }
        NewsRespone newsRespone = newsService.getAllNews(pageNumber, pageSize);
        List<NewsDTO>news = newsRespone.getContent();
        model.addAttribute("news",news);
        model.addAttribute("user", user);
        model.addAttribute("newsCT", newsService.getOneNewsByCode("CHINH-TRI"));
        model.addAttribute("newsGT", newsService.getOneNewsByCode("GIAI-TRI"));
        model.addAttribute("newsSK", newsService.getOneNewsByCode("SUC-KHOE"));
        model.addAttribute("totalItems", newsRespone.getTotalElement());
        model.addAttribute("totalPages", newsRespone.getTotalPage());
        model.addAttribute("pageNumber", pageNumber);
        return "news-list";
    }

    @GetMapping("/category/{category}")
    public String getNewsByCategory(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber
            ,@RequestParam(value = "pageSize", defaultValue = "6", required = false)int pageSize
            ,Model model
            ,@PathVariable(value = "category") String category) {
        UserDTO user = new UserDTO();
        String username = SecurityUtil.getSessionUser();
        if(username != null) {
            user = userService.getUserByUsername(username);
        }
        int id = categoryService.getCategoryIdByCode(category);
        NewsRespone newsRespone = newsService.getNewsByCategoryId(pageNumber, pageSize, id);
        List<NewsDTO>news = newsRespone.getContent();
        model.addAttribute("news",news);
        model.addAttribute("user", user);
        model.addAttribute("totalItems", newsRespone.getTotalElement());
        model.addAttribute("totalPages", newsRespone.getTotalPage());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("categoryCode", category);
        return "news-list-category";
    }

    @GetMapping("/create")
    public String createNewsForm(Model model) {
        String username = SecurityUtil.getSessionUser();
        if(username == null) {
            return "redirect:/auth/login";
        }
        List<CategoryDTO> categoryDTOS = categoryService.getAllCategory();
        NewsDTO newsDTO = new NewsDTO();
        model.addAttribute("newsDTO",newsDTO);
        model.addAttribute("categories", categoryDTOS);
        return "news-create";
    }

    @PostMapping("/create")
    public String createNews(@Valid @ModelAttribute("newsDTO") NewsDTO newsDTO, BindingResult result) {
        if(result.hasErrors()) return "news-create";
        newsService.createNews(newsDTO);
        return "redirect:/news";
    }

    @GetMapping("/search")
    public String searchNews(@RequestParam(value = "query") String query,
                             Model model,
                             @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                             @RequestParam(value = "pageSize", defaultValue = "6", required = false)int pageSize) {
        UserDTO user = new UserDTO();
        String username = SecurityUtil.getSessionUser();
        if(username != null) {
            user = userService.getUserByUsername(username);
        }
        NewsRespone newsRespone = newsService.searchNewsByTitle(pageNumber, pageSize, query);
        model.addAttribute("news", newsRespone.getContent());
        model.addAttribute("user", user);
        model.addAttribute("totalItems", newsRespone.getTotalElement());
        model.addAttribute("totalPages", newsRespone.getTotalPage());
        model.addAttribute("pageNumber", pageNumber);
        return "news-list";
    }

    @GetMapping("/{id}/edit")
    public String updateNews(@PathVariable("id") Integer id, Model model) {
        String username = SecurityUtil.getSessionUser();
        if(username == null) {
            return "redirect:/auth/login";
        }
        NewsDTO newsDTO = newsService.getNewsById(id);
        List<CategoryDTO> categoryDTOS = categoryService.getAllCategory();
        model.addAttribute("newsDTO", newsDTO);
        model.addAttribute("categories", categoryDTOS);
        return "news-edit";
    }

    @PostMapping("/{id}/edit")
    public String updateNews(@PathVariable("id") Integer id
            , @Valid @ModelAttribute("newsDTO") NewsDTO newsDTO
            ,BindingResult result) {
        if(result.hasErrors()) return "news-edit";
        newsService.updateNews(newsDTO, id);
        return "redirect:/news";
    }

    @GetMapping("/{id}")
    public String newsDetail(@PathVariable("id") Integer id ,Model model) {
        NewsDTO newsDTO = newsService.getNewsById(id);
        List<CommentDTO> comments = commentService.getAllCommentByNewsId(id);
        UserDTO user = new UserDTO();
        String username = SecurityUtil.getSessionUser();
        if(username != null) {
            user = userService.getUserByUsername(username);
        }
        CommentDTO commentDTO = new CommentDTO();
        model.addAttribute("newsDTO", newsDTO);
        model.addAttribute("comments", comments);
        model.addAttribute("comment", commentDTO);
        model.addAttribute("user", user);
        return "news-detail.html";
    }

    @PostMapping("/{id}")
    public String newsComment(@PathVariable("id") Integer id, @ModelAttribute("comment") CommentDTO comment, @ModelAttribute("newsDTO") NewsDTO newsDTO) {
        String username = SecurityUtil.getSessionUser();
        if(username == null) {
            return "redirect:/auth/login";
        }
        comment.setNewsId(newsDTO.getId());
        commentService.createComment(comment);
        return "redirect:/news/{id}";
    }

    @GetMapping("/{id}/delete")
    public String deleteNews(@PathVariable("id") Integer id) {
        newsService.deleteNews(id);
        return "redirect:/news";
    }


}
