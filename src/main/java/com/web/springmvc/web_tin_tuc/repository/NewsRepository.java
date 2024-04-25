package com.web.springmvc.web_tin_tuc.repository;

import com.web.springmvc.web_tin_tuc.model.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Integer> {
    @Query("SELECT c FROM News c WHERE c.title LIKE CONCAT('%', :query, '%') ")
    Page<News> searchNewsByTitle(String query, Pageable pageable);

    Page<News> findByCategoryId(int categoryId, Pageable pageable);

}
