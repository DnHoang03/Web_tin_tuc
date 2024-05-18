package com.web.springmvc.web_tin_tuc.controller;

import com.web.springmvc.web_tin_tuc.dto.NewsDTO;
import com.web.springmvc.web_tin_tuc.dto.ListRespone;
import com.web.springmvc.web_tin_tuc.dto.UserDTO;
import com.web.springmvc.web_tin_tuc.service.CategoryService;
import com.web.springmvc.web_tin_tuc.service.NewsService;
import com.web.springmvc.web_tin_tuc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final NewsService newsService;
    private final CategoryService categoryService;
    private final UserService userService;
    @GetMapping
    public String adminMenu(Model model) {
        model.addAttribute("totalNews", newsService.getTotalNews());
        model.addAttribute("totalUser", userService.getTotalUser());
        return "admin/admin-menu";
    }

    @GetMapping("/news")
    public String newsMenu(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber
            ,@RequestParam(value = "pageSize", defaultValue = "12", required = false)int pageSize
            ,Model model) {
        ListRespone listRespone = newsService.getAllNews(pageNumber, pageSize);
        List<NewsDTO> news = listRespone.getContent();
        news = categoryService.setCategoryName(news);
        model.addAttribute("news",news);
        model.addAttribute("totalItems", listRespone.getTotalElement());
        model.addAttribute("totalPages", listRespone.getTotalPage());
        model.addAttribute("pageNumber", pageNumber);
        return "admin/admin-news-menu";
    }

    @GetMapping("/news/manage")
    public String manageNews(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber
            ,@RequestParam(value = "pageSize", defaultValue = "6", required = false)int pageSize
            ,Model model) {
        ListRespone listRespone = newsService.getUnacceptedNews(pageNumber, pageSize);
        List<NewsDTO> news = listRespone.getContent();
        news = categoryService.setCategoryName(news);
        model.addAttribute("news",news);
        model.addAttribute("totalItems", listRespone.getTotalElement());
        model.addAttribute("totalPages", listRespone.getTotalPage());
        model.addAttribute("pageNumber", pageNumber);
        return "admin/admin-news-manage";
    }

    @GetMapping("/news/manage/{id}")
    public String editNews(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("newsDTO",newsService.getNewsById(id));
        return "admin/admin-news-detail";
    }

    @PostMapping("/news/manage/{id}")
    public String editNews(@PathVariable("id") Integer id, @RequestParam("result") String result) {
        if(result.equals("accept")) {
            newsService.setAccept(id, true);
        } else {
            newsService.setAccept(id, false);
        }
        return "redirect:/admin/news/manage";
    }

    @GetMapping("/news/delete/{id}")
    public String deleteNews(@PathVariable("id") Integer id) {
        newsService.deleteNews(id);
        return "redirect:/admin/news";
    }

    @GetMapping("/users")
    public String usersMenu(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber
            ,@RequestParam(value = "pageSize", defaultValue = "6", required = false)int pageSize
            ,Model model) {
        ListRespone<UserDTO> users = userService.getAllUser(pageNumber, pageSize);
        model.addAttribute("users", users.getContent());
        model.addAttribute("totalItems", users.getTotalElement());
        model.addAttribute("totalPages", users.getTotalPage());
        model.addAttribute("pageNumber", pageNumber);
        return "admin/admin-users-menu";
    }

    @GetMapping("/users/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUserById(id);
        return "redirect:/admin/users";
    }
}
