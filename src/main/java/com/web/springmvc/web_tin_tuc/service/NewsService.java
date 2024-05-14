package com.web.springmvc.web_tin_tuc.service;

import com.web.springmvc.web_tin_tuc.config.SecurityUtil;
import com.web.springmvc.web_tin_tuc.dto.NewsDTO;
import com.web.springmvc.web_tin_tuc.dto.NewsRespone;
import com.web.springmvc.web_tin_tuc.exception.CategoryNotFoundException;
import com.web.springmvc.web_tin_tuc.exception.NewsNotFoundException;
import com.web.springmvc.web_tin_tuc.exception.UserNotFoundException;
import com.web.springmvc.web_tin_tuc.model.News;
import com.web.springmvc.web_tin_tuc.model.Role;
import com.web.springmvc.web_tin_tuc.model.User;
import com.web.springmvc.web_tin_tuc.repository.CategoryRepository;
import com.web.springmvc.web_tin_tuc.repository.NewsRepository;
import com.web.springmvc.web_tin_tuc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final NewsScrapService newsScrapService;

    public NewsDTO createNews(NewsDTO newsDTO) {
        String username = SecurityUtil.getSessionUser();
        User user = userRepository.findByUsername(username);
        News addedNews = mapToEntity(newsDTO);
        if(user.getRole().equals(Role.ADMIN)) addedNews.setAccepted(true);
        addedNews.setUser(user);
        addedNews.setCategory(categoryRepository.findById(newsDTO.getCategory()).orElseThrow(() -> new CategoryNotFoundException("Not found category")));
        return mapToDTO(newsRepository.save(addedNews));
    }

    public NewsDTO createScrapNews() {
        NewsDTO scrapNews = newsScrapService.getFirstNews();
        if(scrapNews != null && newsRepository.findByTitle(scrapNews.getTitle()) == null) {
            News addedNews = mapToEntity(scrapNews);
            addedNews.setAccepted(true);
            addedNews.setCategory(categoryRepository.findById(scrapNews.getCategory()).orElseThrow(() -> new CategoryNotFoundException("Not found category")));
            addedNews.setUser(scrapNews.getUser());
            newsRepository.save(addedNews);
        }
        return scrapNews;
    }

    public NewsRespone getAllNews(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<News> newsPage = newsRepository.findAllDsc(pageable);
        List<News> news = newsPage.getContent();
        //Map a list<news> to list<newsDTO>
        List<NewsDTO> newsDTOS = news.stream().map(this::mapToDTO).toList();
        NewsRespone newsRespone = new NewsRespone();
        newsRespone.setContent(newsDTOS);
        newsRespone.setPageNumber(newsPage.getNumber());
        newsRespone.setPageSize(newsPage.getSize());
        newsRespone.setTotalElement(newsPage.getTotalElements());
        newsRespone.setTotalPage(newsPage.getTotalPages());
        newsRespone.setLast(newsPage.isLast());
        return newsRespone;
    }


    public NewsRespone getNewsByCategoryId(int pageNumber, int pageSize, int id) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<News> newsPage = newsRepository.findByCategoryId(id, pageable);
        List<News> news = newsPage.getContent();
        List<NewsDTO> newsDTOS = news.stream().map(this::mapToDTO).toList();
        NewsRespone newsRespone = new NewsRespone();
        newsRespone.setContent(newsDTOS);
        newsRespone.setPageNumber(newsPage.getNumber());
        newsRespone.setPageSize(newsPage.getSize());
        newsRespone.setTotalElement(newsPage.getTotalElements());
        newsRespone.setTotalPage(newsPage.getTotalPages());
        newsRespone.setLast(newsPage.isLast());
        return newsRespone;
    }

    public NewsRespone searchNewsByTitle(int pageNumber, int pageSize, String query) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<News> newsPage = newsRepository.searchNewsByTitle(query, pageable);
        List<News> news = newsPage.getContent();
        List<NewsDTO> newsDTOS = news.stream().map(this::mapToDTO).toList();
        NewsRespone newsRespone = new NewsRespone();
        newsRespone.setContent(newsDTOS);
        newsRespone.setPageNumber(newsPage.getNumber());
        newsRespone.setPageSize(newsPage.getSize());
        newsRespone.setTotalElement(newsPage.getTotalElements());
        newsRespone.setTotalPage(newsPage.getTotalPages());
        newsRespone.setLast(newsPage.isLast());
        return newsRespone;
    }

    public NewsDTO getNewsById(Integer id) {
        News news = newsRepository.findById(id).orElseThrow(()->new NewsNotFoundException("Not found news"));
        return mapToDTO(news);
    }

    public News getNewsModelById(Integer id) {
        return newsRepository.findById(id).orElseThrow(()->new NewsNotFoundException("Not found news"));
    }

    public NewsDTO getOneNewsByCode(String code) {
        return mapToDTO(newsRepository.findOneByCategory(code));
    }

    public NewsDTO updateNews(NewsDTO newsDTO, Integer id) {
        String username = SecurityUtil.getSessionUser();
        User user = userRepository.findByUsername(username);
        News news = newsRepository.findById(id).orElseThrow(()-> new NewsNotFoundException("Not found news"));
        News news1 = mapToEntity(newsDTO);
        news1.setId(id);
        news1.setCreatedDate(news.getCreatedDate());
        news1.setUser(user);
        news1.setCategory(categoryRepository.findById(newsDTO.getCategory()).orElseThrow(() -> new CategoryNotFoundException("Not found category")));
        newsRepository.save(news1);
        return mapToDTO(news1);
    }

    public void deleteNews(Integer id) {
        News news = newsRepository.findById(id).orElseThrow(()-> new NewsNotFoundException("Not found news"));
        newsRepository.deleteById(id);
    }


    private NewsDTO mapToDTO(News news) {
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setId(news.getId());
        newsDTO.setTitle(news.getTitle());
        newsDTO.setThumbnail(news.getThumbnail());
        newsDTO.setContent(news.getContent());
        newsDTO.setShortDescription(news.getShortDescription());
        newsDTO.setCategory(news.getCategory().getId());
        newsDTO.setUser(news.getUser());
        newsDTO.setCreatedDate(formateDatetime(news.getCreatedDate()));
        return newsDTO;
    }

    private News mapToEntity(NewsDTO newsDTO) {
        News news = new News();
        news.setTitle(newsDTO.getTitle());
        news.setThumbnail(newsDTO.getThumbnail());
        news.setContent(newsDTO.getContent());
        news.setShortDescription(newsDTO.getShortDescription());
        return news;
    }

    private String formateDatetime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return localDateTime.format(formatter);
    }
}
