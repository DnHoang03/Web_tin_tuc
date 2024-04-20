package com.web.springmvc.web_tin_tuc.controller;

import com.web.springmvc.web_tin_tuc.config.SecurityUtil;
import com.web.springmvc.web_tin_tuc.dto.NewsDTO;
import com.web.springmvc.web_tin_tuc.dto.NewsRespone;
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
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;
    private final CategoryService categoryService;
    private final UserService userService;
//    @GetMapping
//    public ResponseEntity<NewsRespone> getAllNews(
//            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
//            @RequestParam(value = "pageSize", defaultValue = "5", required = false)int pageSize) {
//        // Tra ve 1 NewsRespone chua list va cac thong tin lien quan
//        return ResponseEntity.ok(newsService.getAllNews(pageNumber, pageSize));
//    }

    @GetMapping
    public String getAllNews(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber
            ,@RequestParam(value = "pageSize", defaultValue = "6", required = false)int pageSize
            ,Model model) {
        User user = new User();
        String username = SecurityUtil.getSessionUser();
        if(username != null) {
            user = userService.getUserByUsername(username);
        }
        NewsRespone newsRespone = newsService.getAllNews(pageNumber, pageSize);
        List<NewsDTO>news = newsRespone.getContent();
        model.addAttribute("news",news);
        model.addAttribute("user", user);
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
        int id = categoryService.getCategoryIdByCode(category);
        NewsRespone newsRespone = newsService.getNewsByCategoryId(pageNumber, pageSize, id);
        List<NewsDTO>news = newsRespone.getContent();
        model.addAttribute("news",news);
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
            return "redirect:/api/auth/login";
        }
        NewsDTO newsDTO = new NewsDTO();
        model.addAttribute("newsDTO",newsDTO);
        return "news-create";
    }

    @PostMapping("/create")
    public String createNews(@Valid @ModelAttribute("newsDTO") NewsDTO newsDTO, BindingResult result) {
        if(result.hasErrors()) return "news-create";
        newsDTO.setCategory(1);
        newsService.createNews(newsDTO);
        return "redirect:/api/news";
    }

    @GetMapping("/search")
    public String searchNews(@RequestParam(value = "query") String query, Model model) {
        List<NewsDTO> news = newsService.searchNewsByTitle(query);
        model.addAttribute("news",news);
        return "news-list";
    }

    @GetMapping("/{id}/edit")
    public String updateNews(@PathVariable("id") Integer id, Model model) {
        String username = SecurityUtil.getSessionUser();
        if(username == null) {
            return "redirect:/api/auth/login";
        }
        NewsDTO newsDTO = newsService.getNewsById(id);
        model.addAttribute("newsDTO", newsDTO);
        return "news-edit";
    }

    @PostMapping("/{id}/edit")
    public String updateNews(@PathVariable("id") Integer id
            , @Valid @ModelAttribute("newsDTO") NewsDTO newsDTO
            ,BindingResult result) {
        newsDTO.setCategory(1);
        if(result.hasErrors()) return "news-edit";
        newsService.updateNews(newsDTO, id);
        return "redirect:/api/news";
    }

    @GetMapping("/{id}")
    public String newsDetail(@PathVariable("id") Integer id ,Model model) {
        NewsDTO newsDTO = newsService.getNewsById(id);
        model.addAttribute("newsDTO", newsDTO);
        return "news-detail.html";
    }

    @GetMapping("/{id}/delete")
    public String deleteNews(@PathVariable("id") Integer id) {
        newsService.deleteNews(id);
        return "redirect:/api/news";
    }


}
