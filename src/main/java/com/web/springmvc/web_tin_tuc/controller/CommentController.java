package com.web.springmvc.web_tin_tuc.controller;

import com.web.springmvc.web_tin_tuc.dto.CommentDTO;
import com.web.springmvc.web_tin_tuc.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{id}")
    public ResponseEntity<List<CommentDTO>> getAllCommentByNewsId(@PathVariable Integer id) {
        return ResponseEntity.ok(commentService.getAllCommentByNewsId(id));
    }

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO) {
        return new ResponseEntity<>(commentService.createComment(commentDTO), HttpStatus.CREATED);
    }

}
