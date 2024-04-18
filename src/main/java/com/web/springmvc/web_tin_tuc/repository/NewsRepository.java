package com.web.springmvc.web_tin_tuc.repository;

import com.web.springmvc.web_tin_tuc.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Integer> {
    List<News> findByCategoryId(int categoryId);
    @Query("SELECT c FROM News c WHERE c.title LIKE CONCAT('%', :query, '%') ")
    List<News> searchNewsByTitle(String query);
}
