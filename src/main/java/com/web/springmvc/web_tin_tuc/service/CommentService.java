package com.web.springmvc.web_tin_tuc.service;

import com.web.springmvc.web_tin_tuc.config.SecurityUtil;
import com.web.springmvc.web_tin_tuc.dto.CommentDTO;
import com.web.springmvc.web_tin_tuc.exception.CommentNotFoundException;
import com.web.springmvc.web_tin_tuc.exception.NewsNotFoundException;
import com.web.springmvc.web_tin_tuc.exception.UserNotFoundException;
import com.web.springmvc.web_tin_tuc.model.Comment;
import com.web.springmvc.web_tin_tuc.model.User;
import com.web.springmvc.web_tin_tuc.repository.CommentRepository;
import com.web.springmvc.web_tin_tuc.repository.NewsRepository;
import com.web.springmvc.web_tin_tuc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final NewsService newsService;
    private final UserService userService;

    public CommentDTO createComment(CommentDTO commentDTO) {
        Comment comment = commentRepository.save(mapToEntity(commentDTO));
        return commentDTO;
    }

    public CommentDTO getCommentById(Integer id) {
        return mapToDTO(commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException("Not found comment")));
    }

    public List<CommentDTO> getAllCommentByNewsId(int id) {
        List<Comment> comments = commentRepository.findByNewsId(id);
        return comments.stream().map(this::mapToDTO).toList();
    }

    public void updateComment(CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(commentDTO.getId()).orElseThrow(()-> new CommentNotFoundException("Not found comment"));
        comment.setContent(commentDTO.getContent());
        commentRepository.save(comment);

    }
    public void deleteCommentById(Integer id) {
        commentRepository.deleteById(id);
    }

    private CommentDTO mapToDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setNewsId(comment.getNews().getId());
        commentDTO.setUserDTO(userService.mapToDTO(comment.getUser()));
        return commentDTO;
    }

    private Comment mapToEntity(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setId(commentDTO.getId());
        comment.setContent(commentDTO.getContent());
        comment.setNews(newsService.getNewsModelById(commentDTO.getNewsId()));
        String username = SecurityUtil.getSessionUser();
        comment.setUser(userRepository.findByUsername(username));
        return comment;
    }
}
