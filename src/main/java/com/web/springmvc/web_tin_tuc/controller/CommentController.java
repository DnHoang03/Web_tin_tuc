package com.web.springmvc.web_tin_tuc.controller;

import com.web.springmvc.web_tin_tuc.dto.CommentDTO;
import com.web.springmvc.web_tin_tuc.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{id}")
    public String editComment(@PathVariable("id") Integer id, Model model) {
        CommentDTO commentDTO = commentService.getCommentById(id);
        model.addAttribute("comment", commentDTO);
        return "comment-edit";
    }
    @PostMapping("/{id}")
    public String editComment(@PathVariable("id") Integer id,  @ModelAttribute("comment") CommentDTO commentDTO) {
        CommentDTO comments = commentService.getCommentById(id);
        comments.setContent(commentDTO.getContent());
        commentService.updateComment(comments);
        return "redirect:/news/"+comments.getNewsId().toString();
    }

    @GetMapping("/{id}/delete")
    public String deleteComment(@PathVariable("id") Integer id, @ModelAttribute("comment") CommentDTO commentDTO) {
        CommentDTO comments = commentService.getCommentById(id);
        commentService.deleteCommentById(id);
        return "redirect:/news/"+comments.getNewsId().toString();
    }
}
