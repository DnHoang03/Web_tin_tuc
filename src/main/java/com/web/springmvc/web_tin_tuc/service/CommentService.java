package com.web.springmvc.web_tin_tuc.service;

import com.web.springmvc.web_tin_tuc.dto.CommentDTO;
import com.web.springmvc.web_tin_tuc.exception.NewsNotFoundException;
import com.web.springmvc.web_tin_tuc.exception.UserNotFoundException;
import com.web.springmvc.web_tin_tuc.model.Comment;
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
    private final NewsRepository newsRepository;
    private final UserRepository userRepository;


    public CommentDTO createComment(CommentDTO commentDTO) {
        Comment comment = commentRepository.save(mapToEntity(commentDTO));
        return commentDTO;
    }

    public List<CommentDTO> getAllCommentByNewsId(int id) {
        List<Comment> comments = commentRepository.findByNewsId(id);
        return comments.stream().map(this::mapToDTO).toList();
    }


    private CommentDTO mapToDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent(comment.getContent());
        commentDTO.setUserId(comment.getUser().getId());
        commentDTO.setNewsId(comment.getNews().getId());
        return commentDTO;
    }

    private Comment mapToEntity(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setNews(newsRepository.findById(commentDTO.getNewsId()).orElseThrow(()-> new NewsNotFoundException("Not found news")));
        comment.setUser(userRepository.findById(commentDTO.getUserId()).orElseThrow(()-> new UserNotFoundException("Not found user")));
        return comment;
    }
}
