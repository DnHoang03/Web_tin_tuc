package com.web.springmvc.web_tin_tuc.service;

import com.web.springmvc.web_tin_tuc.config.SecurityUtil;
import com.web.springmvc.web_tin_tuc.dto.NewsDTO;
import com.web.springmvc.web_tin_tuc.dto.ListRespone;
import com.web.springmvc.web_tin_tuc.exception.CategoryNotFoundException;
import com.web.springmvc.web_tin_tuc.exception.NewsNotFoundException;
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

    public Integer getTotalNews() {
        return (int) newsRepository.count();
    }

    public ListRespone<NewsDTO> getAllNews(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<News> newsPage = newsRepository.findAllDsc(pageable);
        List<News> news = newsPage.getContent();
        //Map a list<news> to list<newsDTO>
        List<NewsDTO> newsDTOS = news.stream().map(this::mapToDTO).toList();
        ListRespone<NewsDTO> listRespone = new ListRespone<NewsDTO>();
        listRespone.setContent(newsDTOS);
        listRespone.setPageNumber(newsPage.getNumber());
        listRespone.setPageSize(newsPage.getSize());
        listRespone.setTotalElement(newsPage.getTotalElements());
        listRespone.setTotalPage(newsPage.getTotalPages());
        listRespone.setLast(newsPage.isLast());
        return listRespone;
    }


    public ListRespone getNewsByCategoryId(int pageNumber, int pageSize, int id) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<News> newsPage = newsRepository.findByCategoryId(id, pageable);
        List<News> news = newsPage.getContent();
        List<NewsDTO> newsDTOS = news.stream().map(this::mapToDTO).toList();
        ListRespone listRespone = new ListRespone();
        listRespone.setContent(newsDTOS);
        listRespone.setPageNumber(newsPage.getNumber());
        listRespone.setPageSize(newsPage.getSize());
        listRespone.setTotalElement(newsPage.getTotalElements());
        listRespone.setTotalPage(newsPage.getTotalPages());
        listRespone.setLast(newsPage.isLast());
        return listRespone;
    }

    public ListRespone<NewsDTO> searchNewsByTitle(int pageNumber, int pageSize, String query) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<News> newsPage = newsRepository.searchNewsByTitle(query, pageable);
        List<News> news = newsPage.getContent();
        List<NewsDTO> newsDTOS = news.stream().map(this::mapToDTO).toList();
        ListRespone listRespone = new ListRespone();
        listRespone.setContent(newsDTOS);
        listRespone.setPageNumber(newsPage.getNumber());
        listRespone.setPageSize(newsPage.getSize());
        listRespone.setTotalElement(newsPage.getTotalElements());
        listRespone.setTotalPage(newsPage.getTotalPages());
        listRespone.setLast(newsPage.isLast());
        return listRespone;
    }

    public ListRespone<NewsDTO> getUnacceptedNews(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<News> newsPage = newsRepository.findUnacceptedNews(pageable);
        List<News> news = newsPage.getContent();
        //Map a list<news> to list<newsDTO>
        List<NewsDTO> newsDTOS = news.stream().map(this::mapToDTO).toList();
        ListRespone listRespone = new ListRespone();
        listRespone.setContent(newsDTOS);
        listRespone.setPageNumber(newsPage.getNumber());
        listRespone.setPageSize(newsPage.getSize());
        listRespone.setTotalElement(newsPage.getTotalElements());
        listRespone.setTotalPage(newsPage.getTotalPages());
        listRespone.setLast(newsPage.isLast());
        return listRespone;
    }

    public void setAccept(Integer id, boolean type) {
        if(type) {
            News news = newsRepository.findById(id).orElseThrow(() -> new NewsNotFoundException("Not found news"));
            news.setAccepted(true);
            newsRepository.save(news);
        } else {
            newsRepository.deleteById(id);
        }
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

    public List<NewsDTO> getNewsByCode(String code) {
        return newsRepository.findByCategoryCode(code).stream().map(this::mapToDTO).toList().subList(0, 12);
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
        newsDTO.setAccepted(news.getAccepted());
        return newsDTO;
    }

    private News mapToEntity(NewsDTO newsDTO) {
        News news = new News();
        news.setTitle(newsDTO.getTitle());
        news.setThumbnail(newsDTO.getThumbnail());
        news.setContent(newsDTO.getContent());
        news.setShortDescription(newsDTO.getShortDescription());
        news.setAccepted(newsDTO.getAccepted());
        return news;
    }

    private String formateDatetime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return localDateTime.format(formatter);
    }

    public ListRespone<NewsDTO> getAllBestNews(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<News> newsPage = newsRepository.findBestNews(pageable);
        List<News> news = newsPage.getContent();
        //Map a list<news> to list<newsDTO>
        List<NewsDTO> newsDTOS = news.stream().map(this::mapToDTO).toList();
        ListRespone listRespone = new ListRespone();
        listRespone.setContent(newsDTOS);
        listRespone.setPageNumber(newsPage.getNumber());
        listRespone.setPageSize(newsPage.getSize());
        listRespone.setTotalElement(newsPage.getTotalElements());
        listRespone.setTotalPage(newsPage.getTotalPages());
        listRespone.setLast(newsPage.isLast());
        return listRespone;
    }
}
